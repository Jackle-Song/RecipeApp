package com.mrsworkshop.recipeapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mrsworkshop.recipeapp.databinding.ActivityRecipeListBinding

class RecipeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}