package com.tcorner.appbrella.util.mapper

import com.android.billingclient.api.Purchase
import com.tcorner.appbrella.domain.model.PurchaseProduct

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

class PurchaseMapper {

    companion object {
        fun toPurchaseProducts(purchases: MutableList<Purchase>?): List<PurchaseProduct> {
            val purchaseItems: ArrayList<PurchaseProduct> = ArrayList()

            if (purchases != null) {
                for (purchase in purchases) {
                    purchaseItems.add(PurchaseProduct(purchaseToken = purchase.purchaseToken))
                }
            }

            return purchaseItems
        }
    }
}
