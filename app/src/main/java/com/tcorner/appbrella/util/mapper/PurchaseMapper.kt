package com.tcorner.appbrella.util.mapper

import com.android.billingclient.api.Purchase
import com.tcorner.appbrella.common.item.PurchaseItem

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

class PurchaseMapper {

    companion object {
        fun toPurchaseItems(purchases: MutableList<Purchase>?): List<PurchaseItem> {
            val purchaseItems: ArrayList<PurchaseItem> = ArrayList()

            if (purchases != null) {
                for (purchase in purchases) {
                    purchaseItems.add(PurchaseItem(token = purchase.purchaseToken))
                }
            }

            return purchaseItems
        }
    }
}
