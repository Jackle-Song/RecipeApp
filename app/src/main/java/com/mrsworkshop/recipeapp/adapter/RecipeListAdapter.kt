package com.mrsworkshop.recipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrsworkshop.recipeapp.R
import com.mrsworkshop.recipeapp.apiData.response.RecipeListData

class RecipeListAdapter(
    private var mContext : Context,
    private var recipeDetailsList : MutableList<RecipeListData>?,
    private var filteredRecipeDetailsList : MutableList<RecipeListData>?,
    private var mListener : RecipeListInterface,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        filteredRecipeDetailsList = recipeDetailsList
    }

    interface RecipeListInterface {
        fun viewRecipeDetails(recipeListData: RecipeListData)
    }

    class RecipeDetailsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgRecipeImage : ImageView = itemView.findViewById(R.id.imgRecipeImage)
        val txtRecipeTitle : TextView = itemView.findViewById(R.id.txtRecipeTitle)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_recipe_list, parent, false)
        return RecipeDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredRecipeDetailsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipeDetailItem = filteredRecipeDetailsList?.get(position)
        val recipeDetailsViewHolder = holder as RecipeDetailsViewHolder

        Glide.with(mContext)
            .load("https:${recipeDetailItem?.Image}")
            .into(recipeDetailsViewHolder.imgRecipeImage)

        recipeDetailsViewHolder.txtRecipeTitle.text = recipeDetailItem?.Title

        recipeDetailsViewHolder.imgRecipeImage.setOnClickListener {
            if (recipeDetailItem != null) {
                mListener.viewRecipeDetails(recipeDetailItem)
            }
        }
    }

    fun updateRecipeList(updatedRecipeList : MutableList<RecipeListData>?) {
        recipeDetailsList = updatedRecipeList ?: mutableListOf()
        filteredRecipeDetailsList = recipeDetailsList?.toMutableList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredRecipeDetailsList?.clear()

        if (query.isEmpty()) {
            filteredRecipeDetailsList?.addAll(recipeDetailsList ?: mutableListOf())
        } else {
            recipeDetailsList?.let { originalList ->
                val filtered = originalList.filter { movies ->
                    movies.Title?.contains(query, true) == true
                }
                filteredRecipeDetailsList?.addAll(filtered)
            }
        }

        notifyDataSetChanged()
    }
}