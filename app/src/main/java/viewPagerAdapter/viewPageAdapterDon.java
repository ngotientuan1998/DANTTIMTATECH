package viewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import frament.DangKiFragment;
import frament.DangNhapFrament;
import frament.FragmentDangGiao;
import frament.FragmentDoiDuyet;
import frament.FragmentDuyet;
import frament.FragmentNhan;

public class viewPageAdapterDon extends FragmentStateAdapter {
    public viewPageAdapterDon(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    public viewPageAdapterDon(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FragmentDoiDuyet();
        } else if (position == 1) {
            return new FragmentDuyet();
        } else if (position == 2) {
            return new FragmentDangGiao();
        } else if (position == 3) {
            return new FragmentNhan();
        }
        return new FragmentDoiDuyet();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
