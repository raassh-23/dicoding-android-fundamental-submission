package com.raassh.dicodinggithubuserapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.raassh.dicodinggithubuserapp.R
import com.raassh.dicodinggithubuserapp.adapter.ListUserAdapter
import com.raassh.dicodinggithubuserapp.data.UserItem
import com.raassh.dicodinggithubuserapp.databinding.ActivityFavoriteBinding
import com.raassh.dicodinggithubuserapp.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.apply {
            setHasFixedSize(true)
            this.layoutManager = layoutManager
            addItemDecoration(
                DividerItemDecoration(
                    this@FavoriteActivity,
                    layoutManager.orientation
                )
            )
        }

        favoriteViewModel.getFavorites().observe(this) {
            if (it != null) {
                binding.apply {
                    rvUsers.adapter = ListUserAdapter(ArrayList(it)).apply {
                        setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                            override fun onItemClicked(user: UserItem) {
                                val detailsIntent =
                                    Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                                        .apply {
                                            putExtra(UserDetailActivity.EXTRA_USER, user)
                                        }
                                startActivity(detailsIntent)
                            }
                        })
                    }

                    tvEmptyText.apply {
                        if (it.isEmpty()) {
                            visibility = View.VISIBLE
                            text = getString(R.string.empty_text, "Favorite")
                        } else {
                            visibility = View.GONE
                        }
                    }
                }
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}