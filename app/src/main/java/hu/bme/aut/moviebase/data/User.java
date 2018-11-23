package hu.bme.aut.moviebase.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "user")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "email")
    public final String email;

    @ColumnInfo(name = "password")
    public final String password;

    @ColumnInfo(name = "money")
    public int money;

    public User(long id, String email, String password, int money) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.money = money;
    }

    @Ignore
    public User(String email, String password, int money) {
        this.email = email;
        this.password = password;
        this.money = money;
    }

    private User(Parcel in) {
        id = in.readLong();
        email = in.readString();
        password = in.readString();
        money = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(money);
    }
}
