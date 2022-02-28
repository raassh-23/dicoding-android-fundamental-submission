package com.raassh.dicodinggithubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.raassh.dicodinggithubuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<User>()

    private val listUsers: ArrayList<User>
        get() {
            val names = resources.getStringArray(R.array.name)
            val usernames = resources.getStringArray(R.array.username)
            val locations = resources.getStringArray(R.array.location)
            val repos = resources.getIntArray(R.array.repository)
            val companies = resources.getStringArray(R.array.company)
            val followers = resources.getIntArray(R.array.followers)
            val followings = resources.getIntArray(R.array.following)
            val photos = resources.obtainTypedArray(R.array.avatar)

            val tempList = ArrayList<User>()
            for (i in names.indices) {
                val user = User(
                    names[i],
                    usernames[i],
                    locations[i],
                    repos[i],
                    companies[i],
                    followers[i],
                    followings[i],
                    photos.getResourceId(i, -1)
                )
                tempList.add(user)
            }

            return tempList
        }

    private lateinit var binding: ActivityMainBinding

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
                Toast.makeText(this@MainActivity, user.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}