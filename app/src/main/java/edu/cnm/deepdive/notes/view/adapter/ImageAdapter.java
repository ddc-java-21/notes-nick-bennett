package edu.cnm.deepdive.notes.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.model.entity.Image;
import java.util.List;

public class ImageAdapter extends ArrayAdapter<Image> {

  public ImageAdapter(@NonNull Context context, @NonNull List<Image> images) {
    super(context, R.layout.item_image, images);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ImageView view = (ImageView) ((convertView != null)
            ? convertView
            : LayoutInflater.from(getContext()).inflate(R.layout.item_image, parent, false));
    Image image = getItem(position);
    view.setImageURI(image.getUri());
    return view;
  }

}
