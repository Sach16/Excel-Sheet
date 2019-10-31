package cn.zhouchaoyuan.excelpaneldemo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zhouchaoyuan.excelpanel.BaseExcelPanelAdapter;
import cn.zhouchaoyuan.excelpaneldemo.bean.Cell;
import cn.zhouchaoyuan.excelpaneldemo.bean.ColTitle;
import cn.zhouchaoyuan.excelpaneldemo.bean.RowTitle;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class CustomAdapter extends BaseExcelPanelAdapter<RowTitle, ColTitle, Cell> {

    private Context context;
    private View.OnClickListener blockListener;
    public static ECListener ecListner;

    public CustomAdapter(Context context, View.OnClickListener blockListener, ECListener ecListener) {
        super(context);
        this.context = context;
        this.blockListener = blockListener;
        this.ecListner = ecListener;
    }

    //=========================================content's cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_normal_cell, parent, false);
        CellHolder cellHolder = new CellHolder(layout);
        return cellHolder;
    }

    @Override
    public void onBindCellViewHolder(RecyclerView.ViewHolder holder, int verticalPosition, int horizontalPosition) {
        Cell cell = getMajorItem(verticalPosition, horizontalPosition);
        Log.v("Cell: " + verticalPosition, ", " + horizontalPosition);
        if (null == holder || !(holder instanceof CellHolder) || cell == null) {
            return;
        }
        CellHolder viewHolder = (CellHolder) holder;
        viewHolder.cellContainer.setTag(cell);
        viewHolder.cellContainer.setOnClickListener(blockListener);
        /*if (cell.getStatus() == 0) {
            viewHolder.bookingName.setText(cell.getBookingName());
            viewHolder.channelName.setText(cell.getChannelName());
            viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.booking));
        } else {
            viewHolder.bookingName.setText(cell.getBookingName());
            viewHolder.channelName.setText(cell.getChannelName());
            if (cell.getStatus() == 1) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.left));
            } else if (cell.getStatus() == 2) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.staying));
            } else if (cell.getStatus() == 3) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.text_dark3));
            } else if (cell.getStatus() == 4) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_color));
            } else if (cell.getStatus() == 5) {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.punk_color));
            } else {
                viewHolder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.booking));
            }
        }*/
        viewHolder.bookingName.setText(cell.getBookingName());
        viewHolder.channelName.setText(cell.getChannelName());
        if (cell.isVisible()) {
            ViewGroup.LayoutParams params = viewHolder.rlMainCell.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            viewHolder.rlMainCell.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = viewHolder.rlMainCell.getLayoutParams();
            params.height = 0;
            params.width = 0;
            viewHolder.rlMainCell.setLayoutParams(params);
        }
    }

    static class CellHolder extends RecyclerView.ViewHolder {

        public final TextView bookingName;
        public final TextView channelName;
        public final LinearLayout cellContainer;
        public final RelativeLayout rlMainCell;

        public CellHolder(View itemView) {
            super(itemView);
            bookingName = (TextView) itemView.findViewById(R.id.booking_name);
            channelName = (TextView) itemView.findViewById(R.id.channel_name);
            cellContainer = (LinearLayout) itemView.findViewById(R.id.pms_cell_container);
            rlMainCell = (RelativeLayout) itemView.findViewById(R.id.rl_main_cell);
        }
    }


    //=========================================top cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateTopViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_top_header_item, parent, false);
        TopHolder topHolder = new TopHolder(layout);
        return topHolder;
    }

    @Override
    public void onBindTopViewHolder(RecyclerView.ViewHolder holder, int position) {
        RowTitle rowTitle = getTopItem(position);
        if (null == holder || !(holder instanceof TopHolder) || rowTitle == null) {
            return;
        }
        TopHolder viewHolder = (TopHolder) holder;
//        viewHolder.columnLabel.setText(rowTitle.getWeekString());
        viewHolder.columnLabel.setText(rowTitle.getBudgetString());
    }

    static class TopHolder extends RecyclerView.ViewHolder {

        public final TextView columnLabel;

        public TopHolder(View itemView) {
            super(itemView);
            columnLabel = (TextView) itemView.findViewById(R.id.column_label);
        }
    }

    //=========================================left cell===========================================
    @Override
    public RecyclerView.ViewHolder onCreateLeftViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_status_left_header_item, parent, false);
        LeftHolder leftHolder = new LeftHolder(layout);
        return leftHolder;
    }

    @Override
    public void onBindLeftViewHolder(RecyclerView.ViewHolder holder, int position) {
        ColTitle colTitle = getLeftItem(position);
        if (null == holder || !(holder instanceof LeftHolder) || colTitle == null) {
            return;
        }
        LeftHolder viewHolder = (LeftHolder) holder;
        viewHolder.roomNumberLabel.setText(colTitle.getRoomNumber());
        viewHolder.roomTypeLabel.setText(colTitle.getRoomTypeName());
        viewHolder.wbsItemLabel.setText(colTitle.getRoomTypeItem());
        if (colTitle.isParent())
            if (colTitle.isExpanded())
                viewHolder.minusPlus.setText("- ");
            else
                viewHolder.minusPlus.setText("+ ");
        else
            viewHolder.minusPlus.setText("");
        if (colTitle.getpId() != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int i = Integer.parseInt(colTitle.getpId());
            params.setMargins((i % 10)*10, 0, 0, 0);
            viewHolder.minusPlus.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            viewHolder.minusPlus.setLayoutParams(params);
        }
        ViewGroup.LayoutParams lp = viewHolder.root.getLayoutParams();
        viewHolder.root.setLayoutParams(lp);
        if (colTitle.isVisible()) {
            ViewGroup.LayoutParams params = viewHolder.root.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, context.getResources().getDisplayMetrics());
            viewHolder.root.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = viewHolder.root.getLayoutParams();
            params.height = 0;
            params.width = 0;
            viewHolder.root.setLayoutParams(params);
        }
    }

    static class LeftHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView roomNumberLabel;
        public final TextView roomTypeLabel;
        public final TextView wbsItemLabel;
        public final TextView minusPlus;
        public final View root;

        public LeftHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            root.setOnClickListener(this);
            minusPlus = (TextView) itemView.findViewById(R.id.minus_plus);
            roomNumberLabel = (TextView) itemView.findViewById(R.id.room_number_label);
            wbsItemLabel = (TextView) itemView.findViewById(R.id.wbs_item_label);
            roomTypeLabel = (TextView) itemView.findViewById(R.id.room_type_label);
        }

        @Override
        public void onClick(View view) {
            ecListner.onecClick(getPosition());
        }
    }

    //=========================================left-top cell===========================================
    @Override
    public View onCreateTopLeftView() {
        return LayoutInflater.from(context).inflate(R.layout.room_status_top_left_item, null);
    }

    public interface ECListener {
        void onecClick(int i);
    }
}
