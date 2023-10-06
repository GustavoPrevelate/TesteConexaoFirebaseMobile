package br.senai.sp.jandira.myuploadfirebase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.senai.sp.jandira.myuploadfirebase.databinding.EachItemBinding
import com.squareup.picasso.Picasso

class ImagesAdapter(private var mList: List<String>):  RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>(){

    inner class ImagesViewHolder(var binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root)

    //CARREGA OS ITENS A SEREM EXIBIDOS
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesAdapter.ImagesViewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ImagesViewHolder(binding)
    }

    //GERA A VISUALIZAÇÃO DOS ITENS
    override fun onBindViewHolder(holder: ImagesAdapter.ImagesViewHolder, position: Int) {
        with(holder.binding){
            with(mList[position]){
                Picasso.get().load(this).into(imageView)
            }
        }
    }

    //CONTAGEM DOS ITENS A SEREM EXIBIDOS
    override fun getItemCount(): Int {
        return mList.size//CONTAGEM DOS ITENS A SEREM EXIBIDOS
    }

}