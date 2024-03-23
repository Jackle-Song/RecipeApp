package com.mrsworkshop.recipeapp.activity

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mrsworkshop.recipeapp.apiData.response.RecipeListData
import com.mrsworkshop.recipeapp.databinding.ActivityRecipeDetailsBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class RecipeDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityRecipeDetailsBinding

    private var recipeListData : RecipeListData = RecipeListData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window?.statusBarColor = Color.TRANSPARENT
        setStatusBarLightTheme(false)

        val recipeDetails = intent.getStringExtra(RecipeListActivity.INTENT_RECIPE_DETAILS)
        recipeListData = Gson().fromJson(recipeDetails, RecipeListData::class.java)

        initUI()
        setupComponentListener()
    }

    /**
     * private function
     */

    private fun initUI() {
        Glide.with(this@RecipeDetailsActivity)
            .load("https:${recipeListData.Image}")
            .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
            .into(binding.imgBlurRecipeImage)
        Glide.with(this@RecipeDetailsActivity)
            .load("https:${recipeListData.Image}")
            .into(binding.imgCardRecipeImage)

        binding.txtRecipeTitle.text = recipeListData.Title
        binding.txtInstructionsDetails.text = recipeListData.Instructions

        if (recipeListData.Ingredients?.isJsonArray == true) {
            val ingredientList : MutableList<String> = Gson().fromJson(recipeListData.Ingredients, MutableList::class.java) as MutableList<String>
            val ingredientsDetailList = StringBuilder()

            ingredientList.forEach { item ->
                ingredientsDetailList.append("$item\n")
            }

            binding.txtIngredientsDetails.text = ingredientsDetailList.toString()
        }
        else if (recipeListData.Ingredients?.isJsonObject == true) {
            val ingredientObject: JsonObject = recipeListData.Ingredients?.asJsonObject ?: JsonObject()
            val ingredientsDetailList = StringBuilder()
            ingredientObject.entrySet().forEach { entry ->
                ingredientsDetailList.append("${entry.value.asString}\n")
            }

            binding.txtIngredientsDetails.text = ingredientsDetailList.toString()
        }
    }

    private fun setupComponentListener() {
        binding.imgCloseBtn.setOnClickListener {
            finish()
        }
    }
}