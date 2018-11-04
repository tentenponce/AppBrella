package com.tcorner.appbrella.ui.common.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mikepenz.fastadapter.items.ModelAbstractItem
import com.tcorner.appbrella.R
import com.tcorner.appbrella.domain.model.Donation
import kotlinx.android.synthetic.main.layout_donate.view.tv_message
import kotlinx.android.synthetic.main.layout_donate.view.tv_price
import kotlinx.android.synthetic.main.layout_donate.view.tv_title

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
class DonateAdapterItem(donation: Donation) :
    ModelAbstractItem<Donation, DonateAdapterItem, DonateAdapterItem.ViewHolder>(donation) {

    override fun getLayoutRes(): Int {
        return R.layout.layout_donate
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun getType(): Int {
        return R.id.product_item
    }

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.itemView.tv_title.text = model.name
        holder.itemView.tv_message.text = model.description
        holder.itemView.tv_price.text = model.price
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}