package com.example.onlineshopapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.onlineshopapp.Adapter.CategoryAdapter
import com.example.onlineshopapp.Adapter.PopularAdapter
import com.example.onlineshopapp.Adapter.SliderAdapter
import com.example.onlineshopapp.Model.SliderModel
import com.example.onlineshopapp.ViewModel.MainViewModel
import com.example.onlineshopapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        initBanner()
        initCategory()
        initRecommended()
        initBottomMenu()
    }

    private fun initBottomMenu() {
        binding.CartBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity,CartActivity::class.java))
        }
    }

    private fun initRecommended() {
        binding.progressBarPopular.visibility=View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.viewPopular.layoutManager=GridLayoutManager(this@MainActivity,2)
            binding.viewPopular.adapter= PopularAdapter(it)
            binding.progressBarPopular.visibility=View.GONE

        })
        viewModel.loadPopular()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility=View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
            binding.viewCategory.adapter= CategoryAdapter(it)
            binding.progressBarCategory.visibility=View.GONE
        })
        viewModel.loadCategory()
    }

    private fun initBanner() {
        binding.progressBarSlider.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer {
            banners(it)
            binding.progressBarSlider.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners(image: List<SliderModel>) {
        binding.viewPager2.adapter= SliderAdapter(image,binding.viewPager2)
        binding.viewPager2.clipToPadding=false
        binding.viewPager2.clipChildren=false
        binding.viewPager2.offscreenPageLimit=3
        binding.viewPager2.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPager2.setPageTransformer(compositePageTransformer)

        if(image.size>1){
            binding.dotIndicator.visibility=View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPager2)
        }
    }
}