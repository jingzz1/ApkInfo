package com.base.mvp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class MVPComposite {
    private List<BasePresenter> presenters = new ArrayList<>();

    public void addPresenter(BasePresenter presenter){
        if(presenters == null)
            presenters = new ArrayList<>();
        presenters.add(presenter);
    }

    public void clear(){
        if(presenters == null ||presenters.size()<=0){
            return;
        }

        for(BasePresenter presenter:presenters){
            presenter.unBindPresenter();
        }
        presenters.clear();
    }
}
