package org.selfconference.android.api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;

public class Speaker implements Parcelable {
    private final int id;
    private final String name;
    private final String twitter;
    private final String bio;
    private final String headshot;
    private final List<Integer> sessions;

    private Speaker(Builder builder) {
        id = builder.id;
        name = builder.name;
        twitter = builder.twitter;
        bio = builder.bio;
        headshot = builder.headshot;
        sessions = builder.sessions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getBio() {
        return bio;
    }

    public String getHeadshot() {
        return headshot;
    }

    public List<Integer> getSessionIds() {
        return sessions;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("twitter", twitter)
                .add("id", id)
                .add("name", name)
                .add("sessions", sessions)
                .toString();
    }

    public static final class Builder {
        private int id = -1;
        private String name = "";
        private String twitter = "";
        private String bio = "";
        private String headshot = "";
        private List<Integer> sessions = newArrayList();

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder twitter(String twitter) {
            this.twitter = twitter;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder headshot(String headshot) {
            this.headshot = headshot;
            return this;
        }

        public Builder sessions(List<Integer> sessions) {
            this.sessions = sessions;
            return this;
        }

        public Speaker build() {
            return new Speaker(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.twitter);
        dest.writeString(this.bio);
        dest.writeString(this.headshot);
        dest.writeList(this.sessions);
    }

    private Speaker(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.twitter = in.readString();
        this.bio = in.readString();
        this.headshot = in.readString();
        this.sessions = newArrayList();
        in.readList(this.sessions, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Speaker> CREATOR = new Parcelable.Creator<Speaker>() {
        public Speaker createFromParcel(Parcel source) {
            return new Speaker(source);
        }

        public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };
}
