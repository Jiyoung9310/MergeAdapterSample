package com.example.mymergeadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.MergeAdapter
import com.example.mymergeadapter.adapter.TagAdapter
import com.example.mymergeadapter.adapter.ListAdapter
import com.example.mymergeadapter.adapter.PostAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val listAdapter : ListAdapter by lazy { ListAdapter() }
    private val postAdapter : PostAdapter by lazy { PostAdapter() }
    private val tagAdapter: TagAdapter by lazy { TagAdapter() }
    private lateinit var mergeAdapter: MergeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        mergeAdapter = MergeAdapter(listAdapter, postAdapter, tagAdapter)
        recyclerView.adapter = mergeAdapter
    }
}
