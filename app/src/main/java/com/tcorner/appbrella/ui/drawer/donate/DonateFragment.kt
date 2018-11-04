package com.tcorner.appbrella.ui.drawer.donate

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.tcorner.appbrella.R
import com.tcorner.appbrella.domain.model.Donation
import com.tcorner.appbrella.domain.model.PurchaseDonation
import com.tcorner.appbrella.ui.base.BaseFragment
import com.tcorner.appbrella.ui.common.adapter.DonateAdapterItem
import com.tcorner.appbrella.util.LoadingMessageUtil
import com.tcorner.appbrella.util.factory.DialogFactory
import com.tcorner.appbrella.util.mapper.PurchaseMapper
import kotlinx.android.synthetic.main.fragment_donate.rv_donate
import kotlinx.android.synthetic.main.fragment_donate.swipe_donate
import javax.inject.Inject

/**
 *
 * Created by Exequiel Egbert V. Ponce on 11/4/2018.
 */
class DonateFragment : BaseFragment(),
    DonateMvpView {

    companion object {
        fun newInstance(): DonateFragment {
            val fragment = DonateFragment()

            val bundle = Bundle()
            fragment.arguments = bundle

            return fragment
        }
    }

    @Inject
    lateinit var mPresenter: DonatePresenter

    @Inject
    lateinit var mBillingClient: BillingClient

    private lateinit var loadingDialog: Dialog

    private var mDonationAdapter: FastItemAdapter<DonateAdapterItem> = FastItemAdapter()
    override fun title(): String {
        return getString(R.string.menu_donate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component().inject(this)
        mPresenter.attachView(this)

        loadingDialog = DialogFactory.createLoadingDialog(context!!, LoadingMessageUtil.getRandomLoadingMessage(context!!))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_donate, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* fast item adapter */
        mDonationAdapter.withOnClickListener { _, _, item, _ ->
            if (mBillingClient.isReady) {
                purchaseDonation(item.model)
            } else {
                loadingDialog.show()

                mBillingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingServiceDisconnected() {
                    }

                    override fun onBillingSetupFinished(responseCode: Int) {
                        if (loadingDialog.isShowing) {
                            loadingDialog.dismiss()
                        }

                        if (responseCode == BillingClient.BillingResponse.OK) {
                            purchaseDonation(item.model)
                        } else if (responseCode == BillingClient.BillingResponse.DEVELOPER_ERROR ||
                            responseCode == BillingClient.BillingResponse.SERVICE_DISCONNECTED) {
                            // either already connecting, or connection has ended. Don't show anything, just let the user click again
                        } else if (responseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE) {
                            Toast.makeText(context, R.string.error_donation_feature, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, getString(R.string.error_generic, responseCode.toString()), Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }

            true
        }

        /* recycler view */
        rv_donate.layoutManager = LinearLayoutManager(context)
        rv_donate.adapter = mDonationAdapter

        /* swipe refresh layout */
        swipe_donate.setOnRefreshListener { mPresenter.getDonations() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mPresenter.getDonations()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
        mBillingClient.endConnection()
    }

    override fun showSwipeLoading() {
        swipe_donate.post { swipe_donate.isRefreshing = true }
    }

    override fun hideSwipeLoading() {
        swipe_donate.post { swipe_donate.isRefreshing = false }
    }

    override fun showDonations(donations: List<Donation>) {
        mDonationAdapter.clear()
        mDonationAdapter.add(donations.map { DonateAdapterItem(it) })
    }

    override fun showGetDonationError(it: Throwable) {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.error_generic_title)
            .setMessage(getString(R.string.error_get_donation, it.javaClass.simpleName))
            .setNegativeButton(R.string.okay) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun errorPurchase(e: Throwable) {
        Toast.makeText(
            context,
            String.format(getString(R.string.error_donation), e.javaClass.simpleName),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun successPurchase(purchaseDonations: List<PurchaseDonation>) {
        AlertDialog.Builder(context!!)
            .setTitle(R.string.success)
            .setMessage(R.string.success_donation)
            .setPositiveButton(R.string.welcome) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun hideLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    /**
     * Consume the purchased item/s immediately
     */
    fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            mPresenter.consumePurchases(PurchaseMapper.toPurchaseProducts(purchases))
        } else if (responseCode == BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED) {
            Toast.makeText(context, R.string.error_donation_feature, Toast.LENGTH_LONG).show()
        } else if (responseCode == BillingClient.BillingResponse.SERVICE_DISCONNECTED ||
            responseCode == BillingClient.BillingResponse.SERVICE_UNAVAILABLE ||
            responseCode == BillingClient.BillingResponse.ERROR) {
            Toast.makeText(context, R.string.error_donation_service, Toast.LENGTH_LONG).show()
//        } else if (responseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE) {
            // api version surely support this
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
//             do nothing when purchase is cancelled
//        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            // this won't happen as we are consuming if item already owned
//        } else if (responseCode == BillingClient.BillingResponse.ITEM_NOT_OWNED) {
            // this won't happen as we are purchasing before consuming
        } else {
            Toast.makeText(context, getString(R.string.error_generic, responseCode.toString()), Toast.LENGTH_LONG).show()
        }
    }

    private fun purchaseDonation(donation: Donation) {
        mBillingClient.launchBillingFlow(
            activity, BillingFlowParams.newBuilder()
            .setSku(donation.id)
            .setType(BillingClient.SkuType.INAPP)
            .build())
    }
}
