package cn.zhouchaoyuan.excelpaneldemo.bean;

/**
 * Created by zhouchaoyuan on 2017/1/14.
 */

public class ColTitle {

    private String roomNumber;
    private String roomTypeName;
    private String roomTypeItem;
    private boolean isVisible = true;
    private boolean isParent;
    private String pId;
    private int children;
    private boolean expanded = true;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getRoomTypeItem() {
        return roomTypeItem;
    }

    public void setRoomTypeItem(String roomTypeItem) {
        this.roomTypeItem = roomTypeItem;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
