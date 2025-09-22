package com.advance.sanatani_vivah.cardStack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.advance.sanatani_vivah.R

class CardStackAdapter(
        private var arrayList: List<CardItem> = emptyList(),
        private var activity: FragmentActivity
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    private var myListener: ItemListener? = null
    private var loadMoreListener: OnLoadMoreListener? = null
    private var isLoading = false
    private var isMoreDataAvailable:kotlin.Boolean = true
    private lateinit var myPageChangeCallback : ViewPager2.OnPageChangeCallback
    lateinit var viewPagerProfile: ViewPager2

    fun getSpots(): List<CardItem> {
        return arrayList
    }

    fun setSpots(spots: List<CardItem>) {
        this.arrayList = spots
    }

    fun setListener(listener: ItemListener) {
        myListener = listener
    }

    interface ItemListener {
        fun previuosClicked(position: Int)
        fun nextClicked(position: Int)
        fun shareClicked(position: Int, item: CardItem?)
        fun sendMessageClicked(position: Int, item: CardItem?)
        fun moreClicked(position: Int, item: CardItem?)
        fun showPhotosClicked(position: Int, item: CardItem?)
        fun callWhatsappClicked(position: Int, item: CardItem?)
        fun connectClicked(position: Int, item: CardItem?)
        fun itemClicked(position: Int, item: CardItem?)
        fun notNowClicked(position: Int, item: CardItem?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.cell_swipe_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList[position]

        if(position == 0){
            holder.btnPrevious.visibility = View.GONE
        }else{
            holder.btnPrevious.visibility = View.VISIBLE
        }

     //   cardProfileOtherUserProfileFragment = CardProfileOtherUserProfileFragment(item, position)

        holder.viewPagerProfile.isUserInputEnabled = false;
        holder.viewPagerProfile.adapter = myListener?.let { CardProfileAdapter(activity, item, it, position) }
        holder.viewPagerProfile.orientation = ViewPager2.ORIENTATION_VERTICAL
    //    holder.viewPagerProfile.registerOnPageChangeCallback(myPageChangeCallback)
        viewPagerProfile = holder.viewPagerProfile
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var viewPagerProfile: ViewPager2
        var btnPrevious: LinearLayout
        var btnNext: LinearLayout

        init {
            viewPagerProfile = itemView.findViewById(R.id.viewPagerProfile)
            btnPrevious = itemView.findViewById(R.id.btnPrevious)
            btnNext = itemView.findViewById(R.id.btnNext)

            btnPrevious.setOnClickListener {
                myListener?.previuosClicked(absoluteAdapterPosition)
            }
            btnNext.setOnClickListener {
                myListener?.nextClicked(absoluteAdapterPosition)
            }
        }

    }

    class CardProfileAdapter(fragmentActivity: FragmentActivity, private val newCardItem: CardItem, private val itemListener: ItemListener, private val adapterPosition: Int) : FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CardProfileImagesFragment(newCardItem, adapterPosition, itemListener)
                else -> {
                    return Fragment();
                }
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    fun setMoreDataAvailable(moreDataAvailable: Boolean) {
        isMoreDataAvailable = moreDataAvailable
    }

    fun notifyDataChanged() {
        notifyDataSetChanged()
        isLoading = false
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    fun setLoadMoreListener(loadMoreListener1: OnLoadMoreListener) {
        loadMoreListener = loadMoreListener1
    }

}
