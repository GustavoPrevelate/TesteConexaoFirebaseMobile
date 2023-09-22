package br.senai.sp.jandira.myuploadfirebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }

    //FUNÇÃO DE UPLOAD
    private fun uploadImage(){

        binding.progressBar.visibility = android.view.View.VISIBLE

        //DEFINE UM NOME UNICO PARA A IMAGEM COM USO DE UM VALOR TIMESTAMP
        storageRef = storageRef.child(System.currentTimeMillis().toString())

        //EXECUTA O PROESSO DE UPLOAD DA IMAGEM
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener {
                task->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this,
                                "UPLOAD CONCLUIDO!",
                                Toast.LENGTH_LONG
                            ).show()
                        }else{
                            Toast.makeText(
                                this,
                                "ERRO AO REALIZAR O UPLOAD!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

            }
            binding.progressBar.visibility = android.view.View.VISIBLE
        }

    }

}

