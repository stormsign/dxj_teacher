package com.dxj.teacher.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dxj.teacher.MainActivity;
import com.dxj.teacher.R;
import com.dxj.teacher.activity.ChatActivity;
import com.dxj.teacher.adapter.ChatAllHistoryAdapter;
import com.dxj.teacher.application.MyApplication;
import com.dxj.teacher.base.BaseFragment;
import com.dxj.teacher.utils.ToastUtils;
import com.dxj.teacher.widget.TitleNavBar;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by khb on 2015/9/10.
 */
public class MessageFragment extends BaseFragment{

//    private RecyclerView list;
    private ListView list;

    private InputMethodManager inputMethodManager;
    private ListView listView;
    private ChatAllHistoryAdapter adapter;
    private EditText query;
    private ImageButton clearSearch;
    public RelativeLayout errorItem;

    public TextView errorText;
    private boolean hidden;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();


    @Override
    public View initView(LayoutInflater inflater) {
//        view = inflater.inflate(R.layout.fragment_message, null);
//        list = (RecyclerView) view.findViewById(R.id.list);
        view = inflater.inflate(R.layout.fragment_message, null);
        initTitle();
        return view;
    }
    private void initTitle(){
        TitleNavBar title = (TitleNavBar) view.findViewById(R.id.title);
        title.setTitle("消息");
        title.setTitleNoRightButton();
        title.disableBack(true);
        title.back.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
//        errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);

        conversationList.addAll(loadConversationsWithRecentChat());
        listView = (ListView) getView().findViewById(R.id.list);
        adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
        // 设置adapter
        listView.setAdapter(adapter);


        final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = adapter.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(MyApplication.getInstance().getUserName()))
                    ToastUtils.showToast(getActivity(), st2);
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if(conversation.isGroup()){
                        if(conversation.getType() == EMConversation.EMConversationType.ChatRoom){
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_CHATROOM);
                            intent.putExtra("groupId", username);
                        }else{
                            // it is group chat
                            try {
                                intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                                intent.putExtra("groupHXId", username)
                                .putExtra("groupHead", conversation.getLastMessage().getStringAttribute("groupHead"))
                                .putExtra("groupName", conversation.getLastMessage().getStringAttribute("groupName"))
                                .putExtra("groupId", conversation.getLastMessage().getStringAttribute("groupId"));
                            } catch (EaseMobException e) {
                                e.printStackTrace();
                            }
                        }

                    }else{
                        // it is single chat
                        intent.putExtra("userId", username);
                    }
                    startActivity(intent);
                }
            }
        });
        // 注册上下文菜单
        registerForContextMenu(listView);

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }

        });

    }

    void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
        getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
        // }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean handled = false;
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
            handled = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
            handled = true;
        }
        EMConversation tobeDeleteCons = adapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        // 删除此会话
        EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
        inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        adapter.remove(tobeDeleteCons);
        adapter.notifyDataSetChanged();

        // 更新消息未读数
//        ((MainActivity) getActivity()).updateUnreadLabel();

        return handled ? true : super.onContextItemSelected(item);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationsWithRecentChat());
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 获取所有会话
     *
     * @param context
     * @return
    +	 */
    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //if(conversation.getType() != EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     *
     * @param usernames
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
//        if (!hidden && ! ((MainActivity)getActivity()).isConflict) {
//            refresh();
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if(((MainActivity)getActivity()).isConflict){
//            outState.putBoolean("isConflict", true);
//        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
//            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
//        }
    }

//    @Override
//    public void onClick(View v) {
//    }









    @Override
    public void initData() {

    }
}
