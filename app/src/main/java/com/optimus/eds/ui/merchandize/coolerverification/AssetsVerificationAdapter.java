package com.optimus.eds.ui.merchandize.coolerverification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.optimus.eds.R;
import com.optimus.eds.db.entities.Asset;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created By apple on 4/30/19
 */
public class AssetsVerificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Asset> assetList;
    private AssetVerificationStatusListener listener;
    private int check=0;

    public AssetsVerificationAdapter(Context context, AssetVerificationStatusListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.assetList = new ArrayList<>();
    }

    public void populateAssets(List<Asset> assets) {
        this.assetList = assets;
        notifyDataSetChanged();
    }

    public List<String> getReasons(boolean isVerified){
        List<String> list = new ArrayList<>();
        list.add("");
        if(!isVerified) {
            list.add("Scanning Not being done");
            list.add("No Asset");
            list.add("No Barcode");
        }
        return list;
    }

    @Override
    public AssetsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.assets_verification_list_item, parent, false);

        return new AssetsListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AssetsListHolder assetsListHolder = (AssetsListHolder) holder;
        Asset asset = assetList.get(position);
        List<String> reasons = getReasons(asset.getVerified());
        ((AssetsListHolder) holder).codeTv.setText(asset.getAssetNumber());
        ((AssetsListHolder) holder).statusTextView.setText(asset.getVerified()?"Verified":"Pending");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (mContext, R.layout.spinner_item, reasons);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);


        assetsListHolder.reasonsSpinner.setAdapter(spinnerArrayAdapter);
        String reason = asset.getReason();
        if(!reason.isEmpty()){
            int index = reasons.indexOf(reason);
            assetsListHolder.reasonsSpinner.setSelection(index);
        }

        assetsListHolder.reasonsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(++check > 1) {
                    asset.setReason(((TextView) view).getText().toString());
                    listener.onStatusChange(asset);
                    Log.i("Errorrrr!!!", position + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    static class AssetsListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAssetCode)
        TextView codeTv;
        @BindView(R.id.tvStatus)
        TextView statusTextView;
        @BindView(R.id.spinnerReason)
        Spinner reasonsSpinner;

        AssetsListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
