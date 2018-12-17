package thiepchuctet.tinnhanchuctet.tetnguyendan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private int mId;
    private String mContent;

    public Message(int id, String content) {
        mId = id;
        mContent = content;
    }

    protected Message(Parcel in) {
        mId = in.readInt();
        mContent = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getId() {
        return mId;
    }

    public Message setId(int id) {
        mId = id;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public Message setContent(String content) {
        mContent = content;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mContent);
    }
}
