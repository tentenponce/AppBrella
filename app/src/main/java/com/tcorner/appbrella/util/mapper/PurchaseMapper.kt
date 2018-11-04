package com.tcorner.appbrella.util.mapper

import com.android.billingclient.api.Purchase
import com.tcorner.appbrella.domain.model.PurchaseDonation

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/2/2018.
 */

class PurchaseMapper {

    companion object {
        fun toPurchaseProducts(purchases: MutableList<Purchase>?): List<PurchaseDonation> {
            val purchaseItems: ArrayList<PurchaseDonation> = ArrayList()

            if (purchases != null) {
                for (purchase in purchases) {
                    purchaseItems.add(PurchaseDonation(purchaseToken = purchase.purchaseToken))
                }
            }

            return purchaseItems
        }
    }
}
