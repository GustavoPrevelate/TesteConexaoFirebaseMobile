package br.senai.sp.jandira.myuploadfirebase

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import br.senai.sp.jandira.myuploadfirebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    //DECLARAÇÃO DOS ATRIBUTOS:

    //ACTIVITYMAINBINDING - MANIPULAÇÃO DOS ELEMENTOS GRÁFICOS DO MATERIAL DESIGN
    private lateinit var binding: ActivityMainBinding

    //STORAGEREFERENCES - PERMITE A MANIPULAÇÃO DO CLOUD STORAGE (ARMAZENA ARQUIVOS)
    private lateinit var storageRef: StorageReference

    //FIREBASEFIRESTORE - PERMITE A MANIPULAÇÃO DO BANCO DE DADOS NOSQL
    private lateinit var firebaseFireStore: FirebaseFirestore

    //URI - PERMITE A MANIPULAÇÃO DE ARQUIVOS ATRAVÉS DO SEU ENDEREÇAMENTO
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initiVars()
        registerclickEvents()

    }

    //FUNÇÃO DE INICIALIZAÇÃO DOS RECURSOS DO FIREBASE
    private fun initiVars(){
        storageRef = FirebaseStorage.getInstance().reference.child("images")
        firebaseFireStore = FirebaseFirestore.getInstance()
    }

    //FUNÇÃO PARA O LANÇADOR DE RECUPERAÇÃO DE IMAGENS DA GALERIA
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){
        imageUri = it
        binding.imageView.setImageURI(it)
    }

    //FUNÇÃO DE TRATAMENTO DE CLICKS
    private fun registerclickEvents(){

        //TRATA O EVENTO DE CLICK DO COMPONENTE IMAGEVIEW
        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")
        }

        //TRATA O EVENTO DE CLICK DO BOTÃO DE UPLOAD
        binding.uploadBtn.setOnClickListener {
            uploadImage()
        }

        //TRATA O EVENTO DE CLICK DO BOTÃO DE LISTAR
        binding.showAllBtn.setOnClickListener {
            startActivity(Intent(this, ImagesFeed::class.java))
        }

    }

//    //FUNÇÃO DE UPLOAD
    private fun uploadImage(){

        binding.progressBar.visibility = android.view.View.VISIBLE

        //DEFINE UM NOME UNICO PARA A IMAGEM COM USO DE UM VALOR TIMESTAMP
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        //EXECUTA O PROESSO DE UPLOAD DA IMAGEM
//        imageUri?.let {
//            storageRef.putFile(it).addOnCompleteListener {
//                task->
//                        if(task.isSuccessful){
//                            Toast.makeText(
//                                this,
//                                "UPLOAD CONCLUIDO!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }else{
//                            Toast.makeText(
//                                this,
//                                "ERRO AO REALIZAR O UPLOAD!",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//
//                binding.progressBar.visibility = View.GONE
//                binding.imageView.setImageResource(R.drawable.upload)
//
//            }
//
//        }

    //GRAVAÇÃO E PROCESSO DE UPLOAD DE IMAGEM NO FIRESTORE V1

    ///// PROCESSO DE UPLOAD - V2 /////
    imageUri?.let {
        storageRef.putFile(it).addOnCompleteListener { task->

            if (task.isSuccessful) {

                storageRef.downloadUrl.addOnSuccessListener { uri ->

                    val map = HashMap<String, Any>()
                    map["pic"] = uri.toString()

                    firebaseFireStore.collection("images").add(map).addOnCompleteListener { firestoreTask ->

                        if (firestoreTask.isSuccessful){
                            Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()

                        }
                        binding.progressBar.visibility = View.GONE
                        binding.imageView.setImageResource(R.drawable.upload)

                    }
                }

            }else{

                Toast.makeText(this,  task.exception?.message, Toast.LENGTH_SHORT).show()

            }

            //BARRA DE PROGRESSO DO UPLOAD
            binding.progressBar.visibility = View.GONE

            //TROCA A IMAGEM PARA A IMAGEM PADRÃO
            binding.imageView.setImageResource(R.drawable.upload)

        }
    }
    ///// PROCESSO DE UPLOAD - V2 /////


    }





}

