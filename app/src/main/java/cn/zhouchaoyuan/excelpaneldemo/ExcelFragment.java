package cn.zhouchaoyuan.excelpaneldemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cn.zhouchaoyuan.excelpanel.ExcelPanel;
import cn.zhouchaoyuan.excelpaneldemo.bean.Cell;
import cn.zhouchaoyuan.excelpaneldemo.bean.ColTitle;
import cn.zhouchaoyuan.excelpaneldemo.bean.RowTitle;

/**
 * Created by zhouchaoyuan on 2017/3/30.
 */

public class ExcelFragment extends Fragment implements ExcelPanel.OnLoadMoreListener, CustomAdapter.ECListener {

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final String WEEK_FORMAT_PATTERN = "EEEE";
    public static final String[] CHANNEL = {"Original", "Pending", "Approved", "Progress", "Tally"};
    public static final String[] NAME = {"$99,000", "$98,000", "$89,000", "$79,000", "$59,000", "$49,000", "$39,000",
            "$67,000", "$34,000", "$48,000", "$73,000", "$51,000", "$85,000", "$88,000"};
    public static final long ONE_DAY = 24 * 3600 * 1000;
    public static final int PAGE_SIZE = 14;
    public static final int ROW_SIZE = 1000;

    private ExcelPanel excelPanel;
    private ProgressBar progress;
    private CustomAdapter adapter;
    private List<RowTitle> rowTitles;
    private List<ColTitle> colTitles;
    private List<List<Cell>> cells;
    private SimpleDateFormat dateFormatPattern;
    private SimpleDateFormat weekFormatPattern;
    private boolean isLoading;
    private long historyStartTime;
    private long moreStartTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.excel_fragment, container, false);
        progress = (ProgressBar) root.findViewById(R.id.progress);
        excelPanel = (ExcelPanel) root.findViewById(R.id.content_container);
        adapter = new CustomAdapter(getActivity(), blockListener, this);
        excelPanel.setAdapter(adapter);
        excelPanel.setOnLoadMoreListener(this);
        excelPanel.addOnScrollListener(onScrollListener);
        initData();
        return root;
    }

    private ExcelPanel.OnScrollListener onScrollListener = new ExcelPanel.OnScrollListener() {
        @Override
        public void onScrolled(ExcelPanel excelPanel, int dx, int dy) {
            super.onScrolled(excelPanel, dx, dy);
            Log.e("acjiji", dx + "     " + dy);
        }
    };

    private View.OnClickListener blockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Cell cell = (Cell) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("code", colTitles.get(cell.getY()).getRoomTypeName());
            bundle.putString("item", colTitles.get(cell.getY()).getRoomTypeItem());
            bundle.putString("budget", cell.getBookingName());
            bundle.putString("status", cell.getChannelName());
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            /*if (cell != null) {
                if (cell.getStatus() == 0) {
                    Toast.makeText(getActivity(), "空房", Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 1) {
                    Toast.makeText(getActivity(), "已离店，离店人：" + cell.getBookingName(), Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 2) {
                    Toast.makeText(getActivity(), "入住中，离店人：" + cell.getBookingName(), Toast.LENGTH_SHORT).show();
                } else if (cell.getStatus() == 3) {
                    Toast.makeText(getActivity(), "预定中，离店人：" + cell.getBookingName(), Toast.LENGTH_SHORT).show();
                }
            }*/
        }
    };

    @Override
    public void onLoadMore() {
        if (!isLoading) {
            loadData(moreStartTime, false);
        }
    }

    @Override
    public void onLoadHistory() {
        if (!isLoading) {
            loadData(historyStartTime, true);
        }
    }

    private void initData() {
        moreStartTime = Calendar.getInstance().getTimeInMillis();
        historyStartTime = moreStartTime - ONE_DAY * PAGE_SIZE;
        dateFormatPattern = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
        rowTitles = new ArrayList<>();
        colTitles = new ArrayList<>();
        cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            cells.add(new ArrayList<Cell>());
        }
        loadData(moreStartTime, false);
    }

    private void loadData(long startTime, final boolean history) {
        //模拟网络加载
        isLoading = true;
        Message message = new Message();
        message.arg1 = history ? 1 : 2;
        message.obj = new Long(startTime);
        loadDataHandler.sendMessageDelayed(message, 1000);
    }

    private Handler loadDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isLoading = false;
            long startTime = (Long) msg.obj;
            List<RowTitle> rowTitles1 = genRowData(startTime);
            List<List<Cell>> cells1 = genCellData();
            if (msg.arg1 == 1) {//history
                historyStartTime -= ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(0, rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(0, cells1.get(i));
                }

                //加载了数据之后偏移到上一个位置去
                if (excelPanel != null) {
                    excelPanel.addHistorySize(PAGE_SIZE);
                }
            } else {
                moreStartTime += ONE_DAY * PAGE_SIZE;
                rowTitles.addAll(rowTitles1);
                for (int i = 0; i < cells1.size(); i++) {
                    cells.get(i).addAll(cells1.get(i));
                }
            }
            if (colTitles.size() == 0) {
                colTitles.addAll(genColData());
            }
            progress.setVisibility(View.GONE);
            adapter.setAllData(colTitles, rowTitles, cells);
//            adapter.enableFooter();
//            adapter.enableHeader();
        }
    };

    //====================================模拟生成数据==========================================
    private List<RowTitle> genRowData(long startTime) {
        List<RowTitle> rowTitles = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < PAGE_SIZE; i++) {
            RowTitle rowTitle = new RowTitle();
            rowTitle.setAvailableRoomCount(random.nextInt(10) + 10);
            rowTitle.setDateString(dateFormatPattern.format(startTime + i * ONE_DAY));
            rowTitle.setWeekString(weekFormatPattern.format(startTime + i * ONE_DAY));
            rowTitle.setBudgetString(CHANNEL[random.nextInt(CHANNEL.length)]);
            rowTitles.add(rowTitle);
        }
        return rowTitles;
    }

    private List<ColTitle> genColData() {
        List<ColTitle> colTitles = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            ColTitle colTitle = new ColTitle();
            /*if (i < 10) {
                colTitle.setRoomNumber("10" + i);
            } else {
                colTitle.setRoomNumber("20" + (i - 10));
            }*/
            colTitle.setRoomNumber(String.valueOf(i + 1));
            Random random = new Random();
            int number = random.nextInt(9999);
            colTitle.setRoomTypeItem("00-" + number);
            if (i % 3 == 0) {
                colTitle.setRoomTypeName("Row-" + i);
            } else if (i % 3 == 1) {
                colTitle.setRoomTypeName("Row-" + i);
            } else {
                colTitle.setRoomTypeName("Row-" + i);
            }
            if (i == 3) {
                colTitle.setParent(true);
                colTitle.setChildren(4);
            }
            if (i == 4) {
                colTitle.setParent(true);
                colTitle.setChildren(3);
                colTitle.setpId(String.valueOf(i));
            }
            if (i == 5) {
                colTitle.setParent(true);
                colTitle.setChildren(2);
                colTitle.setpId(String.valueOf(i));
            }
            if (i == 6 || i == 7) {
                colTitle.setpId(String.valueOf(5));
            }
            if (i == 15) {
                colTitle.setParent(true);
                colTitle.setChildren(1);
            }
            if (i == 16) {
                colTitle.setpId(String.valueOf(i));
            }
            colTitles.add(colTitle);
        }
        return colTitles;
    }

    private List<List<Cell>> genCellData() {
        List<List<Cell>> cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> cellList = new ArrayList<>();
            cells.add(cellList);
            for (int j = 0; j < PAGE_SIZE; j++) {
                Cell cell = new Cell();
                Random random = new Random();
                int number = random.nextInt(6);
                if (number == 1 || number == 2 || number == 3 ||
                        number == 4 || number == 5 || number == 6) {
                    cell.setStatus(number);
                    cell.setChannelName(CHANNEL[random.nextInt(CHANNEL.length)]);
                    cell.setBookingName(NAME[random.nextInt(NAME.length)]);
                } else {
                    cell.setStatus(0);
                    cell.setChannelName(CHANNEL[random.nextInt(CHANNEL.length)]);
                    cell.setBookingName(NAME[random.nextInt(NAME.length)]);
                }
                cell.setX(j);
                cell.setY(i);
                cellList.add(cell);
            }
        }
        return cells;
    }

    @Override
    public void onecClick(int i) {
        if (colTitles.get(i).isParent()) {
            boolean finite = colTitles.get(i + 1).isVisible();
            for (int c = 1; c <= colTitles.get(i).getChildren(); c++)
                if (finite) {
                    colTitles.get(i + c).setVisible(false);
                    for (int j = 0; j < cells.get(i + c).size(); j++) {
                        cells.get(i + c).get(j).setVisible(false);
                    }
                } else {
                    colTitles.get(i + c).setVisible(true);
                    for (int j = 0; j < cells.get(i + c).size(); j++) {
                        cells.get(i + c).get(j).setVisible(true);
                    }
                }
            /*for (int c = 1; c <= colTitles.get(i).getChildren(); c++)
                if (cells.get(i + c).get(0).isVisible()) {
                    colTitles.get(i + c).setVisible(false);
                    for (int j = 0; j < cells.get(i + c).size(); j++) {
                        cells.get(i + c).get(j).setVisible(false);
                    }
                } else {
                    colTitles.get(i + c).setVisible(true);
                    for (int j = 0; j < cells.get(i + c).size(); j++) {
                        cells.get(i + c).get(j).setVisible(true);
                    }
                }*/
            for (int c = 0; c < colTitles.get(i).getChildren(); c++)
                if (colTitles.get(i + c).isParent())
                    if (colTitles.get(i + c + 1).isVisible())
                        colTitles.get(i + c).setExpanded(true);
                    else
                        colTitles.get(i + c).setExpanded(false);
            adapter.setAllData(colTitles, rowTitles, cells);
            adapter.notifyDataSetChanged();
        }
    }
}
