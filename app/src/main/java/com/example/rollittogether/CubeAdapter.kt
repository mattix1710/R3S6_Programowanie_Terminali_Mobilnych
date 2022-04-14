package com.example.rollittogether

import android.content.Context
import android.content.SharedPreferences
import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class CubeAdapter: BaseAdapter {
    private lateinit var mContext: Context
    //private lateinit var cubeCount: EditText
    lateinit var sharedPreferences: SharedPreferences

    // Constructor
    constructor(c: Context) {
        mContext = c;
        sharedPreferences = c.getSharedPreferences(MainActivity.MyPREFERENCES,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    public override fun getCount(): Int{
        if(sharedPreferences.contains(MainActivity.cubeQuantity))
            return sharedPreferences.getInt(MainActivity.cubeQuantity, 3)
        return 3
    }

    public override fun getItem(position: Int): Object? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var imageView: ImageView

        if(convertView == null){
            imageView = ImageView(mContext)
            imageView.setLayoutParams(GridView@ AbsListView.LayoutParams(200, 200))
            imageView.setBackgroundResource(R.drawable.border)
            imageView.setTag(R.drawable.border)
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
            imageView.setPadding(8,8,8,8)
        }
        else{
            imageView = convertView as ImageView            //type casting??
        }

        imageView.setImageResource(mThumbIds[position])
        return  imageView
    }

    // Keep all Images in array
    public val mThumbIds: IntArray = intArrayOf(R.drawable.dice_1,
                                                R.drawable.dice_3,
                                                R.drawable.dice_4,
                                                R.drawable.dice_2,
                                                R.drawable.dice_5,
                                                R.drawable.dice_6,
                                                //temporary solution - set up to 10 cubes...
                                                R.drawable.dice_1,
                                                R.drawable.dice_1,
                                                R.drawable.dice_1,
                                                R.drawable.dice_1)
}