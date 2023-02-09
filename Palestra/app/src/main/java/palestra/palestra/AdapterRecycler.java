package palestra.palestra;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {

    private int[] id_category;
    private TypedArray images;
    private int RED_ZONE_AGE = 17;
    private final int RED_CATEGORY_ID = 1;
    private String participant = "false";

    private static List<Event> events;

    private Resources res;
    private Context context;
    private UserInformation userInformation;

    public AdapterRecycler(List<Event> events, int[] id_category, Context context) {
        this.events = events;
        this.images = images;
        this.id_category = id_category;
        this.context = context;
    }

    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_user_name;
        private TextView tv_category;
        private TextView tv_name_event;
        private TextView tv_brief_information;
        private TextView tv_age_limit;
        private TextView tv_item_card_tv_category;

        private ImageView avatar_creator_event;
        private ImageView item_card_image_event_ramka;

        private LinearLayout lin_age_limit;
        private LinearLayout lin_event;
        private ImageView iv_event;

        public ViewHolder(View v) {
            super(v);

            tv_age_limit = (TextView)v.findViewById(R.id.tv_age_limit);
            tv_user_name = (TextView) v.findViewById(R.id.item_card_tv_user_name);
            tv_category = (TextView) v.findViewById(R.id.item_card_tv_category);
            tv_name_event = (TextView) v.findViewById(R.id.item_card_tv_name_event);
            tv_brief_information = (TextView) v.findViewById(R.id.item_detail_tv_brief_information);
            tv_item_card_tv_category = (TextView)v.findViewById(R.id.item_card_tv_category);
            iv_event = (ImageView) v.findViewById(R.id.item_card_image_event);
            avatar_creator_event = (ImageView)v.findViewById(R.id.avatar_creator_event);
            item_card_image_event_ramka = (ImageView)v.findViewById(R.id.item_card_image_event_ramka);

            lin_age_limit = (LinearLayout)v.findViewById(R.id.background_age_limit);
            lin_event = (LinearLayout)v.findViewById(R.id.linearLayout);

            //progressBar = (ProgressBar)v.findViewById(R.id.progressBar_card);
           // progressBar.getIndeterminateDrawable().setColorFilter(v.getResources().getColor(R.color.blue_light), PorterDuff.Mode.MULTIPLY);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ActivityDetailCategory.class);
                    //TODO:создать класс - структуру для события

                    userInformation = UserInformation.getInstance();

                    GetParticipant getParticipant = new GetParticipant();
                    if(getAdapterPosition() < 0){
                        return;
                    }
                    getParticipant.execute(userInformation.getUserID(),events.get(getAdapterPosition()).getEventID());

                    intent.putExtra(ActivityDetailCategory.EVENT_ID, (events.get(getAdapterPosition()).getEventID()));
                    intent.putExtra(ActivityDetailCategory.TITLE, (events.get(getAdapterPosition()).getTitle()));
                    intent.putExtra(ActivityDetailCategory.BRIEF_INFO, (events.get(getAdapterPosition()).getBriefInfo()));
                    intent.putExtra(ActivityDetailCategory.DATE_OF, (events.get(getAdapterPosition()).getDate()));
                    intent.putExtra(ActivityDetailCategory.AGE_LIMIT, (events.get(getAdapterPosition()).getAgeLimit()));
                    intent.putExtra(ActivityDetailCategory.LATITUDE, (events.get(getAdapterPosition()).getLatitude()));
                    intent.putExtra(ActivityDetailCategory.LONGITUDE, (events.get(getAdapterPosition()).getLongitude()));
                    //intent.putExtra(ActivityDetailCategory.USER_NAME, (events[getAdapterPosition()].getUserName()));
                    //intent.putExtra(ActivityDetailCategory.EVENT_CATEGORY, (events[getAdapterPosition()].getEventCategory()));

                    intent.putExtra(ActivityDetailCategory.COUNT_REVIEW, (events.get(getAdapterPosition()).getCountReview()));
                    intent.putExtra(ActivityDetailCategory.COUNT_PARTICIPANT, (events.get(getAdapterPosition()).getCountParticipant()));
                    //String s = events.get(getAdapterPosition()).getNameCreator();
                    intent.putExtra(ActivityDetailCategory.CREATOR_NAME,(events.get(getAdapterPosition()).getNameCreator()));

                    intent.putExtra(ActivityDetailCategory.EVENT_CATEGORY, (events.get(getAdapterPosition()).getEventCategoryID()));
                    intent.putExtra(ActivityDetailCategory.CREATOR_ID, (events.get(getAdapterPosition()).getIdCreator()));
                    intent.putExtra(ActivityDetailCategory.AVATAR_ID, (events.get(getAdapterPosition()).getAvatarID()));
                    intent.putExtra(ActivityDetailCategory.USER_CATEGORY, userInformation.getUserCategory());

//                    Bitmap image=((BitmapDrawable)iv_event.getDrawable()).getBitmap();
//                    //Convert to byte array
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//////
//                   intent.putExtra(ActivityDetailCategory.BACKGROUND_ID, byteArray);
//                    intent.putExtra(ActivityDetailCategory.BACKGROUND_ID,(
//                            images.getResourceId(
//                                    getPositionImage(
//                                            events.get(getAdapterPosition()).getEventCategoryID()), -1)));

                    try {
                        getParticipant.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra(ActivityDetailCategory.PARTICIPANT,(participant));

                    context.startActivity(intent);
                }
            });
        }
    }

    class GetParticipant extends AsyncTask<Integer,Void,Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            ConnectionToWCF connectionToWCF = ConnectionToWCF.getInstance();
            WorkingWithJSON workingWithJSON;

            SoapTask request = new SoapTask(connectionToWCF.NAMESPACE, connectionToWCF.METHOD_CHECK_PARTICIPANT);
            request.AddProperty("userID", params[0]);
            request.AddProperty("eventID", params[1]);
            SoapObject resultsRequestSOAP =
                    request.startSoap(connectionToWCF.URL, connectionToWCF.SOAP_ACTION(connectionToWCF.METHOD_CHECK_PARTICIPANT));

            if (resultsRequestSOAP != null) {
                if (resultsRequestSOAP.getProperty(0) != null) {
                    String response = resultsRequestSOAP.getProperty(0).toString();
                    workingWithJSON = new WorkingWithJSON(response);
                    participant = workingWithJSON.getParticipant();
                }
            }
            return null;
        }
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public AdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_category, parent, false);
        res = parent.getResources();
        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == -1 || position > events.size())
            return;


        //holder.tv_category.setText(events.get(position).getEventCategoryName());
        holder.tv_category.setText(events.get(position).getEventCategoryName());
        //holder.iv_event.setImageResource(images.getResourceId(getPositionImage(events.get(position).getEventCategoryID()), -1));

        holder.tv_user_name.setText(events.get(position).getNameCreator());
        holder.tv_name_event.setText(events.get(position).getTitle());
        holder.tv_brief_information.setText(events.get(position).getBriefInfo());

        holder.item_card_image_event_ramka.setImageDrawable(res.getDrawable(R.drawable.user_event_ramka));
        holder.item_card_image_event_ramka.setVisibility(View.INVISIBLE);

        userInformation = UserInformation.getInstance();
        //holder.iv_event.setScaleType(ImageView.ScaleType.FIT_XY);
        if(events.get(position).getIdCreator() == userInformation.getUserID()){
            holder.iv_event.setPadding(20, 20, 20, 20);
            holder.item_card_image_event_ramka.setVisibility(View.VISIBLE);
           // holder.item_card_image_event_ramka.setImageDrawable(res.getDrawable(R.drawable.user_event_ramka));
        } else {
            holder.iv_event.setBackgroundColor(res.getColor(R.color.hint));
            holder.iv_event.setPadding(0,0,0,0);
        }

        //holder.iv_event.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context)
                .load("http://lowelly525-001-site1.itempurl.com/images/background/"+
                        events.get(position).getEventCategoryID()+".png")
                //.memoryPolicy(MemoryPolicy.NO_CACHE)
                //.networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.iv_event);
        //возрастное ограничение
        if (events.get(position).getAgeLimit() > RED_ZONE_AGE) {
            holder.lin_age_limit.setBackgroundResource(R.drawable.rectangel_category_red_limit);
            holder.tv_age_limit.setTextColor(res.getColor(R.color.red_limit));
        } else {
            holder.lin_age_limit.setBackgroundResource(R.drawable.rectangel_category_blue_limit);
            holder.tv_age_limit.setTextColor(res.getColor(R.color.blue_light));
        }
        //запрещенная категория
        if (events.get(position).getEventCategoryID() == RED_CATEGORY_ID) {
            holder.lin_event.setBackgroundResource(R.drawable.rectangel_category_red_limit);
            holder.tv_item_card_tv_category.setTextColor(res.getColor(R.color.red_limit));
        } else {
            holder.lin_event.setBackgroundResource(R.drawable.rectangel_category_blue_limit);
            holder.tv_item_card_tv_category.setTextColor(res.getColor(R.color.blue_light));
        }
        Picasso.with(context)
                .load("http://lowelly525-001-site1.itempurl.com/images/150px/face_"+
                events.get(position).getAvatarID()+".png")
                //.memoryPolicy(MemoryPolicy.NO_CACHE)
                //.networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.avatar_creator_event);

        holder.tv_age_limit.setText(String.valueOf(events.get(position).getAgeLimit()) + "+");

        //holder.progressBar.setEnabled(false);
        //holder.progressBar.setVisibility(View.INVISIBLE);
    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        int count = 0;
        if (events != null) {
            count = events.size();
        }
        return count;
    }
}