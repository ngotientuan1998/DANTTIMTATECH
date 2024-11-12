package viewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import frament.DangKiFragment;
import frament.DangNhapFrament;

public class viewPageAdapterDNDK extends FragmentStateAdapter {

    public viewPageAdapterDNDK(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public viewPageAdapterDNDK(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==1){
            return new DangKiFragment();
        }
        return new DangNhapFrament();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
