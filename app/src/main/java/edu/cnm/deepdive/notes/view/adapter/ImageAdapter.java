package edu.cnm.deepdive.notes.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.model.entity.Image;
import java.util.List;

public class ImageAdapter extends Adapter<ViewHolder> {

  private final LayoutInflater inflater;
  private final List<Image> images;

  public ImageAdapter(@NonNull Context context, @NonNull List<Image> images) {
    inflater = LayoutInflater.from(context);
    this.images = images;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ImageHolder(inflater.inflate(R.layout.item_image, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((ImageHolder) holder).bind(position, images.get(position));
  }

  @Override
  public int getItemCount() {
    return images.size();
  }

  private static class ImageHolder extends ViewHolder {

    private final ImageView imageView;

    ImageHolder(@NonNull View itemView) {
      super(itemView);
      imageView = (ImageView) itemView;
    }

    void bind(int position, Image image) {
      imageView.setImageURI(image.getUri());
    }

  }

}
