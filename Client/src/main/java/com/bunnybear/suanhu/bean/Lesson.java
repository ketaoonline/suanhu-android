package com.bunnybear.suanhu.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Lesson implements Parcelable {

    int id;//本小节id
    String sension;//小节（第一节）
    String name;//本小节名字
    int type;//0 视频 1音频 2图文
    int buy;//0 未购买 1 已购买 2 可试看
    String url;
    String text_url;

    public String getText_url() {
        return text_url;
    }

    public String getUrl() {
        return url;
    }

    public int getBuy() {
        return buy;
    }

    public int getType() {
        return type;
    }

    public Lesson(String name) {
        this.name = name;
    }

    protected Lesson(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSension() {
        return sension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return getName() != null ? getName().equals(lesson.getName()) : lesson.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
}


