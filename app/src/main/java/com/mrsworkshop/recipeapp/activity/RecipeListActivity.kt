package com.mrsworkshop.recipeapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mrsworkshop.recipeapp.R
import com.mrsworkshop.recipeapp.adapter.RecipeListAdapter
import com.mrsworkshop.recipeapp.apiData.response.RecipeListData
import com.mrsworkshop.recipeapp.databinding.ActivityRecipeListBinding
import com.mrsworkshop.recipeapp.viewModel.RecipeApiData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeListActivity : BaseActivity(), RecipeListAdapter.RecipeListInterface {
    private lateinit var binding: ActivityRecipeListBinding
    private lateinit var recipeListAdapter: RecipeListAdapter
    private lateinit var mAuth : FirebaseAuth

    private var recipeApiData : RecipeApiData = RecipeApiData()
    private var recipeDetailsList : MutableList<RecipeListData>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        initUI()
        setupComponentListener()
    }

    override fun viewRecipeDetails(recipeListData: RecipeListData) {
        // do nothing
    }

    /**
     * private function
     */

    private fun initUI() {
        recipeListAdapter = RecipeListAdapter(this@RecipeListActivity, recipeDetailsList, recipeDetailsList, this@RecipeListActivity)
        binding.recyclerviewMovieList.layoutManager = GridLayoutManager(this@RecipeListActivity, 2)
        binding.recyclerviewMovieList.adapter = recipeListAdapter

        val spinnerItems = resources.getStringArray(R.array.spinnerRecipeTypeItems)
        val adapter = ArrayAdapter(this, R.layout.layout_spinner_list, spinnerItems)
        adapter.setDropDownViewResource(R.layout.layout_spinner_list)
        binding.spinnerRecipeTypeList.adapter = adapter

        binding.spinnerRecipeTypeList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val selectedItem = spinnerItems[position]
                getRecipeListApi(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun setupComponentListener() {
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mAuth.currentUser != null) {
                    finishAffinity()
                }
            }
        })

        binding.imgLogout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this@RecipeListActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.etEditTextSearchRecipe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recipeListAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }

    /**
     * api call
     */

    private fun getRecipeListApi(query : String?) {
        showLoadingViewDialog()
        recipeDetailsList?.clear()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = recipeApiData.getRecipeList(query)?.execute()
                withContext(Dispatchers.Main) {
                    if (response?.isSuccessful == true) {
                        val recipeResponse = response.body()
                        recipeResponse?.d?.let { recipeDetailsList?.addAll(it) }
                        recipeListAdapter.updateRecipeList(recipeDetailsList)
                        dismissLoadingViewDialog()
                    }
                    else {
                        // Handle unsuccessful response
                        dismissLoadingViewDialog()
                    }
                }
            }
            catch (e: Exception) {
                // Handle network errors
                println("Error occurred during API call: ${e.message}")
                dismissLoadingViewDialog()
            }
        }
    }
}