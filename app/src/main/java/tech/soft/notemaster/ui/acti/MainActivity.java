package tech.soft.notemaster.ui.acti;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import tech.soft.notemaster.R;
import tech.soft.notemaster.controls.DatabaseHelper;
import tech.soft.notemaster.models.Note;
import tech.soft.notemaster.service.QuickWorkService;
import tech.soft.notemaster.ui.adapter.NoteMainAdapter;
import tech.soft.notemaster.ui.calback.IOnItemClick;
import tech.soft.notemaster.utils.IConstand;

import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends BaseActivity
        implements NavigationView.
        OnNavigationItemSelectedListener,
        View.OnClickListener,
        IConstand{

    private static final String TAG = "mMainActivity";
    public static final String ACTION = "ACTION";
    public static final String WRITE = "WRITE";
    public static final String EDIT_NOTE = "EDIT_NOTE";
    public static final String DATA = "DATA";
    private FloatingActionButton fabHandrite;
    private FloatingActionButton fabText;
    private FloatingActionsMenu fabMenu;
    private CoordinatorLayout clMain;
    private RecyclerView rvMain;
    private NoteMainAdapter mAdapter;
    private List<Note> noteList;
    private List<Note> noteTmp;
    private SearchView searchView;
    private int currentPos;


    @Override
    protected void initComponents() {


                DatabaseHelper.getINSTANCE(this).copyNoteDatabaseToSystem();
                noteList = new ArrayList<>();
                noteList = DatabaseHelper.getINSTANCE(this).getListNote();

                noteTmp = new ArrayList<>(noteList);
                Log.d(TAG,noteList.size() + "");
                mAdapter = new NoteMainAdapter(this, noteList, new IOnItemClick() {
                    @Override
                    public void onItemClick(int pos) {
                        Note note = noteTmp.get(pos);
                        switch (note.getType()){
                            case TYPE_TEXT:

                                Intent intentTextNote =new Intent(MainActivity.this,TextNoteActivity.class);
                                intentTextNote.setAction(EDIT_NOTE);
                                intentTextNote.putExtra(DATA,note);
                                startActivity(intentTextNote);

                                break;
                            case TYPE_HAND_DWRAW:
                                Log.d("zzzz","ppasdasd");
                                Intent intentDrawNote = new Intent(MainActivity.this,DrawNoteActivity.class);
                                intentDrawNote.setAction(EDIT_NOTE);intentDrawNote.putExtra(DATA,note);
                                intentDrawNote.putExtra(DATA,note);
                                startActivity(intentDrawNote);
                        }
                    }

                    @Override
                    public void onItemLongClick(int pos, View v) {
                        registerForContextMenu(v);
                        v.showContextMenu();
                        currentPos = pos;
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("STOP_SERVICE");
        sendBroadcast(intent);
    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fb);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initFloatingButton();

        rvMain = (RecyclerView) findViewById(R.id.rvMain);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMain.setLayoutManager(manager);
        rvMain.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    public void initFloatingButton(){
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_multiAction);
        fabHandrite = (FloatingActionButton) findViewById(R.id.action_handrite);
        fabText = (FloatingActionButton) findViewById(R.id.action_text);
        clMain = (CoordinatorLayout) findViewById(R.id.clMain);

        fabText.setOnClickListener(this);
        fabHandrite.setOnClickListener(this);

        clMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               if (fabMenu.isExpanded()){
                   fabMenu.collapse();
               }
                return true;
            }
        });
    }

    @Override
    protected void setEventViews() {

    }

    @Override
    protected void setViewRoot() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteTmp.clear();

                for (int i = 0 ; i < noteList.size() ; i++){
                    if (noteList.get(i).getLabel().contains(newText)){
                        noteTmp.add(noteList.get(i));
                    }
                }
                mAdapter.setNoteList(noteTmp);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater()
                .inflate(R.menu.menu_context_main, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_item:
                boolean isSuccess =
                        DatabaseHelper.getINSTANCE(this).deleteRow(noteTmp.get(currentPos).getId());
                if (isSuccess){
                    noteTmp.remove(currentPos);
                    mAdapter.setNoteList(noteTmp);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this,"Delete Success" ,Toast.LENGTH_LONG).show();
                    noteTmp = DatabaseHelper.getINSTANCE(this).getListNote();
                    noteList = DatabaseHelper.getINSTANCE(this).getListNote();;
                } else {
                    Toast.makeText(this,"Delete failed" ,Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle("Delete All")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                DatabaseHelper.getINSTANCE(MainActivity.this).deleteAll();
                                noteList.clear();
                                noteTmp.clear();
                                mAdapter.setNoteList(noteTmp);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_quick:
                startService(new Intent(this, QuickWorkService.class));
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openTextNoteActivity(String action){
        Intent intentToNoteActivity =
                new Intent(this,TextNoteActivity.class);
        intentToNoteActivity.setAction(action);
        this.finish();
        startActivity(intentToNoteActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_handrite:
                openDrawNoteActivity();
                break;
            case R.id.action_text:
                openTextNoteActivity(WRITE);
                break;
        }
    }

    private void openDrawNoteActivity() {
        Intent intentDraw = new Intent(this,DrawNoteActivity.class);
        intentDraw.setAction(WRITE);
        startActivity(intentDraw);

    }
}
