package com.raassh.dicodinggithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.raassh.dicodinggithubuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    private val listUsers: ArrayList<User>
        get() {
            val names = resources.getStringArray(R.array.name)
            val usernames = resources.getStringArray(R.array.username)
            val locations = resources.getStringArray(R.array.location)
            val repos = resources.getStringArray(R.array.repository)
            val companies = resources.getStringArray(R.array.company)
            val followers = resources.getStringArray(R.array.followers)
            val followings = resources.getStringArray(R.array.following)
            val photos = resources.obtainTypedArray(R.array.avatar)

            val tempList = ArrayList<User>()
            for (i in names.indices) {
                val user = User(
                    usernames[i],
                    names[i],
                    locations[i],
                    repos[i].toInt(),
                    companies[i],
                    followers[i].toInt(),
                    followings[i].toInt(),
                    photos.getResourceId(i, -1)
                )

                tempList.add(user)
            }

            return tempList
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.setHasFixedSize(true)

        list.addAll(listUsers)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val detailsIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                    .apply {
                        putExtra(UserDetailActivity.EXTRA_USER, user)
                    }
                startActivity(detailsIntent)
            }
        })
    }
}