
# MergeAdapter 내용 읽어보기

[원본](https://developer.android.com/reference/androidx/recyclerview/widget/MergeAdapter) 

**MergeAdapter**는 여러 어댑터들의 내용을 순서대로 표시하는 RecyclerView.Adapter 구현체이다.

    MyAdapter adapter1 = ...;
    AnotherAdapter adapter2 = ...;
    MergeAdapter merged = new MergeAdapter(adapter1, adapter2);
    recyclerView.setAdapter(mergedAdapter);

기본적으로 MergeAdapter는 중첩된 어댑터들의 뷰타입을 서로 분리한다. 그래서 추가된 어댑터의 뷰타입 간 충돌을 피하기 위해 RecyclerView로 다시 보내기 전에 뷰타입을 변경한다. 또한, 추가된 각 어댑터에는 자체적으로 분리된 ViewHolder Pool 이 있으며, 추가된 어댑터 간 재사용을 할 수 없다. ~~adapter1과 adapter2의 풀이 따로 관리된다는 얘기~~

만약 어댑터들이 같은 뷰타입을 공유하고 추가된 어댑터들 사이에 ViewHolder 공유가 필요하다면, MergeAdapter.Config.isolateViewTypes를 `false` 로 세팅하여 MergeAdapter.Config 인스턴스를 제공할 수 있다. (~~아래에 내용 있음~~) 일반적인 사용 패턴으로는 RecyclerView.Adapter.getItemViewType(int) 메소드의 return 값으로 R.layout.<layout_name> 을 반환하는 것이다.

추가된 어댑터가 notify 메소드를 호출 시, MergeAdapter가 값을 정확히 계산하여 RecyclerView에 보낸다. 만약 어댑터가 RecyclerView.Adapter.notifyDataSetChanged()를 호출하면, MergeAdapter에서도 RecyclerView.Adapter.notifyDataSetChanged()를 호출한다.  RecyclerView.Adapter.notifyDataSetChanged() 호출을 피할 수 있게 되도록이면 **SortedList 나 ListAdapter를 사용할 것을 권고한다.**

MergeAdapter의 stable ids 지원여부는 MergeAdapter.Config의 설정에 따라 달라진다. RecyclerView.Adapter.setHasStableIds(boolean)를 호출해도 아무런 효력이 없다. stable ids를 사용하기 위한 MergeAdapter 구성의 자세한 내용은 [MergeAdapter.Config.StableIdMode](https://developer.android.com/reference/androidx/recyclerview/widget/MergeAdapter.Config.StableIdMode) 설명서를 참조하라. (~~아래에 내용 있음~~) 디폴트 설정은 stable ids를 사용하지 않고, 하위 어댑터의 stable ids는 무시된다. **위 경우와 마찬가지로 ListAdapter를 사용하는 것이 좋은데,** ListAdapter는 데이터 셋의 변경 사항을 자동으로 계산해서 stable ids가 필요없기 때문이다.

ViewHolder에서 유저 액션을 처리하기 위해서 ViewHolder의 어댑터 position을 찾는 것이 일반적인데, 그런 경우엔 RecyclerView.ViewHolder.getAdapterPosition()을 호출하는 대신 `RecyclerView.ViewHolder.getBindingAdapterPosition()`을 사용해라. 만약 어댑터가 ViewHolder를 공유하는 경우, RecyclerView.ViewHolder.getBindingAdapter()메소드를 사용하면 해당 ViewHolder를 마지막으로 바인드한 어댑터를 찾을 수 있다. 
 

 ## MergeAdapter.Config 내용 읽어보기

 [원본](https://developer.android.com/reference/androidx/recyclerview/widget/MergeAdapter.Config)

### isolateViewTypes
만약 값이 `false`이면, MergeAdapter는 할당된 모든 어댑터가 동일한 전역 뷰타입 풀을 공유한다고 가정하고, 동일한 ViewHolder를 참조하기 위해 동일한 뷰타입을 사용하게 된다.

이 값을 `false`로 설정하면 중첩된 어댑터가 ViewHolder를 공유할 수 있게 된다. 하지만, 이러한 어댑터에는 충돌되는 뷰타입이 없어야 한다. 두개의 다른 어댑터가 서로 다른 ViewHolder에 대해 같은 뷰타입을 리턴하는 경우가 생길 수 있기 때문이다. 기본값은 `true`로 MergeAdapter가 어댑터에서 뷰타입을 분리하여 같은 ViewHolder 사용을 못하게끔 방지한다.

### stableIdMode
MergeAdapter가 stable ids를 지원할지 여부에 대해 정의한다.

3가지 옵션이 있다.
- **StableIdMode.NO_STABLE_IDS** : MergeAdapter는 하위 어댑터가 보내는 stable ids를 무시한다. 디폴트 설정값.
- **StableIdMode.ISOLATED_STABLE_IDS** : MergeAdapter는 RecyclerView.Adapter.hasStableIds()에 대해 true를 리턴하며, 추가된 모든 어댑터들은 stable ids를 가진다. 두개의 다른 어댑터는 서로 알지 못하기 때문에 같은 stable ids를 리턴할 수 있으므로, MergeAdapter는 각 어댑터의 id pool 을 서로 분리하여 RecyclerView에 보내지기 전 보고된 stable id를 덮어쓴다. *~~무슨 말이지~~* 이 모드에서 RecyclerView.ViewHolder.getItemId()의 리턴값은 RecyclerView.Adapter.getItemId(int)의 리턴값과 다를 수 있다.
- **StableIdMode.SHARED_STABLE_IDS** : MergeAdapter는 RecyclerView.Adapter.hasStableIds()에 대해 true를 리턴하며, 추가된 모든 어댑터들은 stable ids를 가진다. 그러나 ISOLATED_STABLE_IDS 모드와는 달리, 리턴된 item ids를 재정의하지 않는다. 자식 어댑터들은 서로를 인지하고, 어댑터 간 item을 이동하지 않는 한 절대 같은 id를 리턴해서는 안된다. 

> Written with [StackEdit](https://stackedit.io/).
