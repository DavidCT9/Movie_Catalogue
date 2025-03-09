package com.example.practice2_3

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MovieAdapter(private val context: Activity, private val arrayList: ArrayList<Movies>)
    :ArrayAdapter<Movies>(context, R.layout.item, arrayList)
{
    @SuppressLint("SuspiciousIndentation")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val inflater:LayoutInflater = LayoutInflater.from(context)
    val view: View = inflater.inflate(R.layout.item, null)

        val currentMovie = arrayList[position]

        view.findViewById<TextView>(R.id.name).text = currentMovie.name ?: "Unknown"
        view.findViewById<TextView>(R.id.year).text = currentMovie.year ?: "Unknown"
        view.findViewById<TextView>(R.id.genre).text = currentMovie.genre ?: "Unknown"
        view.findViewById<TextView>(R.id.latitude).text = currentMovie.latitude.toString()
        view.findViewById<TextView>(R.id.longitude).text = currentMovie.longitude.toString()


        if (view.findViewById<TextView>(R.id.genre).text == "action"){
            view.findViewById<ImageView>(R.id.img).setImageResource(R.drawable.action)
        }else if(view.findViewById<TextView>(R.id.genre).text == "drama"){
            view.findViewById<ImageView>(R.id.img).setImageResource(R.drawable.drama)
        }else if(view.findViewById<TextView>(R.id.genre).text == "comedy"){
            view.findViewById<ImageView>(R.id.img).setImageResource(R.drawable.comedy)
        }


        //        return super.getView(position, convertView, parent)
    return view
    }

}