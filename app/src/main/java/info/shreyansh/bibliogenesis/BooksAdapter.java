package info.shreyansh.bibliogenesis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 *
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context mContext;
    private List<Books> booksList;
    static int t;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Books books = booksList.get(position);
                    String b = books.getName();

                    StorageReference storageRef = storage.getReferenceFromUrl("gs://bookstore-f7b94.appspot.com/");
                    StorageReference bookRef = storageRef.child(b);

                    String fileName = b + " Extract.pdf";
                    StorageReference spaceRef = bookRef.child(fileName);
                    spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Toast.makeText(mContext, "Downloading Extract", Toast.LENGTH_SHORT).show();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            mContext.startActivity(browserIntent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(mContext, b , Toast.LENGTH_SHORT).show();

                 //   Snackbar.make(v, b, Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            });

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Books books = booksList.get(position);
                    String b = books.getName();

                    StorageReference storageRef = storage.getReferenceFromUrl("gs://bookstore-f7b94.appspot.com/");
                    StorageReference bookRef = storageRef.child(b);

                    String fileName = b + " Extract.pdf";
                    StorageReference spaceRef = bookRef.child(fileName);
                    spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            Toast.makeText(mContext, "Downloading Extract", Toast.LENGTH_SHORT).show();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            mContext.startActivity(browserIntent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(mContext, b , Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    public BooksAdapter(Context mContext, List<Books> booksList) {
        this.mContext = mContext;
        this.booksList = booksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Books books = booksList.get(position);
        holder.title.setText(books.getName());
        holder.count.setText(books.getAuthor());

        // loading album cover using Glide library
        Glide.with(mContext).load(books.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
                t = position;

            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_books, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Books books = booksList.get(t);
            final String b = books.getName();
            final String author = books.getAuthor();

            switch (menuItem.getItemId()) {
                case R.id.action_buy:
//                    Books books = booksList.get(t);
  //                  String b = books.getName();

                    StorageReference storageRef = storage.getReferenceFromUrl("gs://bookstore-f7b94.appspot.com/");
                    StorageReference bookRef = storageRef.child(b);

                    String fileName = b + ".pdf";
                    StorageReference spaceRef = bookRef.child(fileName);

                    /*
                    final long ONE_MEGABYTE = 1024 * 1024;
                    spaceRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Data for "images/island.jpg" is returns, use this as needed
                            OutputStream out;
		                    try {

			                    out = new FileOutputStream("temp.pdf");
                                out.Write(bytes,0,bytes.Length);
                                Out.Position = 0;
		                        out.close();
		                        System.out.println("write success");
		                    }catch (Exception e) {
			                    System.out.println(e);
		                    }

                            Toast.makeText(mContext, "Successfully bought", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
*/


/*                  File localFile = null;
                    try {
                        localFile = File.createTempFile(b, "pdf");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    spaceRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                            Toast.makeText(mContext, "Successfully bought", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
*/

                    spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();
                            int i = email.indexOf('@');
                            email = email.substring(0,i);
                            DatabaseReference myRef = database.getReference(email);
                            myRef.child("Books").child(b).setValue(author);


                            Toast.makeText(mContext, "Successfully bought", Toast.LENGTH_SHORT).show();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                            mContext.startActivity(browserIntent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    return true;
                case R.id.action_add_wishlist:
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    String email = user.getEmail();
                    int i = email.indexOf('@');
                    email = email.substring(0,i);
                    DatabaseReference myRef = database.getReference(email);
                    myRef.child("Wishlist").child(b).setValue(author);
                    Toast.makeText(mContext, "Add to Wishlist", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_remove_wishlist:
                    auth = FirebaseAuth.getInstance();
                    user = auth.getCurrentUser();
                    email = user.getEmail();
                    i = email.indexOf('@');
                    email = email.substring(0,i);
                    myRef = database.getReference(email);
                    myRef.child("Wishlist").child(b).removeValue();
                    Toast.makeText(mContext, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }
}