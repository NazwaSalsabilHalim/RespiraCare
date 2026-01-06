package com.example.respiracare

import ai.onnxruntime.*
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.respiracare.databinding.ActivityPrediksiBinding
import java.io.File
import java.io.FileOutputStream
import java.nio.FloatBuffer

class Prediksi : AppCompatActivity() {

    private lateinit var binding: ActivityPrediksiBinding
    private lateinit var env: OrtEnvironment
    private lateinit var session: OrtSession

    private lateinit var symptomsMap: Map<String, String>
    private lateinit var genderMap: Map<String, String>

    private val featureNames = arrayOf(
        "Symptoms_clean_breathing_problem",
        "Symptoms_clean_chest_pain",
        "Symptoms_clean_congestion",
        "Symptoms_clean_cough",
        "Symptoms_clean_fatigue",
        "Symptoms_clean_fever",
        "Disease_clean_asthma",
        "Disease_clean_bronchitis",
        "Disease_clean_flu_virus",
        "Disease_clean_pneumonia",
        "Disease_clean_tuberculosis",
        "Nature_low",
        "Nature_medium",
        "Nature_high",
        "Sex_female",
        "Sex_male",
        "__padding__"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrediksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ONNX Runtime
        env = OrtEnvironment.getEnvironment()
        val modelFile = File(getDir("models", MODE_PRIVATE), "decision_tree_treatment.onnx")
        copyAsset("decision_tree_treatment.onnx", modelFile)
        session = env.createSession(modelFile.absolutePath, OrtSession.SessionOptions())

        // Mapping gejala & jenis kelamin
        symptomsMap = mapOf(
            "Sesak Napas" to "breathing_problem",
            "Batuk" to "cough",
            "Demam" to "fever",
            "Kelelahan" to "fatigue",
            "Hidung Tersumbat" to "congestion",
            "Nyeri Dada" to "chest_pain"
        )
        genderMap = mapOf(
            "Laki-laki" to "male",
            "Perempuan" to "female"
        )

        // Set spinner
        setSpinner(binding.spinnerSymptoms, symptomsMap.keys.toList())
        setSpinner(binding.spinnerSex, genderMap.keys.toList())

        // Tombol Prediksi
        binding.btnPredict.setOnClickListener { predict() }
    }

    private fun predict() {
        val name = binding.etName.text.toString().trim()
        val age = binding.etAge.text.toString().toIntOrNull()
        val selectedSymptom = binding.spinnerSymptoms.selectedItem?.toString()
        val selectedGender = binding.spinnerSex.selectedItem?.toString()

        if (name.isEmpty() || age == null || selectedSymptom.isNullOrEmpty() || selectedGender.isNullOrEmpty()) {
            Toast.makeText(this, "Semua field harus diisi dengan benar!", Toast.LENGTH_SHORT).show()
            return
        }

        val symptoms = symptomsMap[selectedSymptom] ?: "cough"
        val sex = genderMap[selectedGender] ?: "male"

        // Tentukan penyakit berdasarkan gejala
        val disease = when (symptoms) {
            "breathing_problem" -> "asthma"
            "chest_pain" -> "pneumonia"
            "fever", "congestion" -> "flu_virus"
            "cough" -> "bronchitis"
            else -> "flu_virus"
        }

        // Tentukan tingkat keparahan
        val nature = when {
            symptoms == "chest_pain" || symptoms == "breathing_problem" -> "high"
            age < 12 || age > 60 -> "medium"
            else -> "low"
        }

        // Buat input one-hot untuk model ONNX
        val input = FloatArray(featureNames.size) { 0f }

        fun setOneHot(name: String) {
            val idx = featureNames.indexOf(name)
            if (idx != -1) input[idx] = 1f
        }

        setOneHot("Symptoms_clean_$symptoms")
        setOneHot("Disease_clean_$disease")
        setOneHot("Nature_$nature")
        setOneHot("Sex_$sex")

        // Jalankan prediksi ONNX
        val tensor = OnnxTensor.createTensor(env, FloatBuffer.wrap(input), longArrayOf(1, featureNames.size.toLong()))
        val output = session.run(mapOf("float_input" to tensor))
        val outputValue = output[0].value

        val rawResult = when (outputValue) {
            is LongArray -> outputValue[0].toInt()
            is FloatArray -> outputValue[0].toInt()
            is Array<*> -> (outputValue[0] as Number).toInt()
            is Number -> outputValue.toInt()
            else -> 0
        }

        val result = when (rawResult) {
            0 -> "Rest"
            1 -> "Medication"
            2 -> "Hospitalization"
            3 -> "Lifestyle"
            else -> "Rest"
        }

        // Mapping nama penyakit & severity ke bahasa indonesia
        val diseaseIdMap = mapOf(
            "asthma" to "Asma",
            "bronchitis" to "Bronkitis",
            "flu_virus" to "Flu",
            "pneumonia" to "Pneumonia",
            "tuberculosis" to "Tuberkulosis"
        )
        val natureIdMap = mapOf(
            "low" to "Ringan",
            "medium" to "Sedang",
            "high" to "Berat"
        )

        // Kirim hasil ke ResultActivity
        val intent = Intent(this, Result::class.java).apply {
            putExtra("EXTRA_NAME", name)
            putExtra("EXTRA_DISEASE", diseaseIdMap[disease])
            putExtra("EXTRA_SEVERITY", natureIdMap[nature])
            putExtra("EXTRA_TREATMENT", result)
        }
        startActivity(intent)

        // Tutup resource ONNX
        tensor.close()
    }

    private fun setSpinner(sp: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adapter
    }

    private fun copyAsset(name: String, dest: File) {
        if (!dest.exists()) {
            assets.open(name).use { input ->
                FileOutputStream(dest).use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}