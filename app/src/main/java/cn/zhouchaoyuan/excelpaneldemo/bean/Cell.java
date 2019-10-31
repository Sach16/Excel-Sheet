package cn.zhouchaoyuan.excelpaneldemo.bean;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class Cell {

    private int status;// 0没信息 ，3表示预定，2表示入住，1表示离店
    private String channelName;//渠道名称,
    private String bookingName;//预定人姓名
    private boolean isVisible = true;
    public int getStatus() {
        return status;
    }
    public int x, y;

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
