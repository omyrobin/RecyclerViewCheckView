package com.module.checkview.expandablerecyclerview.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * The backing data object for an {@link ExpandableGroup}
 */
public class ExpandableGroup<T extends Parcelable> implements Parcelable {
  private List<T> items;

  public ExpandableGroup(List<T> items) {
    this.items = items;
  }

  public List<T> getItems() {
    return items;
  }

  public int getItemCount() {
    return items == null ? 0 : items.size();
  }

  @Override
  public String toString() {
    return "ExpandableGroup{" +
        ", items=" + items +
        '}';
  }

  protected ExpandableGroup(Parcel in) {
    byte hasItems = in.readByte();
    int size = in.readInt();
    if (hasItems == 0x01) {
      items = new ArrayList<T>(size);
      Class<?> type = (Class<?>) in.readSerializable();
      in.readList(items, type.getClassLoader());
    } else {
      items = null;
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    if (items == null) {
      dest.writeByte((byte) (0x00));
      dest.writeInt(0);
    } else {
      dest.writeByte((byte) (0x01));
      dest.writeInt(items.size());
      final Class<?> objectsType = items.get(0).getClass();
      dest.writeSerializable(objectsType);
      dest.writeList(items);
    }
  }

  @SuppressWarnings("unused")
  public static final Creator<ExpandableGroup> CREATOR =
      new Creator<ExpandableGroup>() {
        @Override
        public ExpandableGroup createFromParcel(Parcel in) {
          return new ExpandableGroup(in);
        }

        @Override
        public ExpandableGroup[] newArray(int size) {
          return new ExpandableGroup[size];
        }
      };
}
