package de.kumpelblase2.remoteentities.nms;

import net.minecraft.server.v1_6_R2.*;
import de.kumpelblase2.remoteentities.entities.RemotePlayerEntity;
import de.kumpelblase2.remoteentities.utilities.NMSUtil;

public class PlayerNavigation extends Navigation {
    // --- Taken from minecraft/Navigation.java
    // --- Modified to work with an entity living
    private EntityLiving a;
    private World b;
    private PathEntity c;
    private double d;
    private AttributeInstance e;
    private boolean f;
    private int g;
    private int h;
    private Vec3D i = Vec3D.a(0.0D, 0.0D, 0.0D);

    private boolean j = true;
    private boolean k;
    private boolean l;
    private boolean m;

    public PlayerNavigation(EntityLiving paramEntityInsentient, World paramWorld) {
        super(NMSUtil.getTempInsertientEntity(), paramWorld);
        this.a = paramEntityInsentient;
        this.b = paramWorld;
        this.e = paramEntityInsentient
                .getAttributeInstance(GenericAttributes.b);
    }

    public void a(boolean paramBoolean) {
        this.l = paramBoolean;
    }

    public boolean a() {
        return this.l;
    }

    public void b(boolean paramBoolean) {
        this.k = paramBoolean;
    }

    public void c(boolean paramBoolean) {
        this.j = paramBoolean;
    }

    public boolean c() {
        return this.k;
    }

    public void d(boolean paramBoolean) {
        this.f = paramBoolean;
    }

    public void a(double paramDouble) {
        this.d = paramDouble;
    }

    public void e(boolean paramBoolean) {
        this.m = paramBoolean;
    }

    public float d() {
        if(this.e == null) {
            return 10.0f;
        }
        return (float) this.e.getValue();
    }

    public PathEntity a(double paramDouble1, double paramDouble2,
            double paramDouble3) {
        if (!l())
            return null;
        return this.b.a(this.a, MathHelper.floor(paramDouble1),
                (int) paramDouble2, MathHelper.floor(paramDouble3), d(),
                this.j, this.k, this.l, this.m);
    }

    public boolean a(double paramDouble1, double paramDouble2,
            double paramDouble3, double paramDouble4) {
        PathEntity localPathEntity = a(MathHelper.floor(paramDouble1),
                (int) paramDouble2, MathHelper.floor(paramDouble3));
        return a(localPathEntity, paramDouble4);
    }

    public PathEntity a(Entity paramEntity) {
        if (!l())
            return null;
        return this.b.findPath(this.a, paramEntity, d(), this.j, this.k,
                this.l, this.m);
    }

    public boolean a(Entity paramEntity, double paramDouble) {
        PathEntity localPathEntity = a(paramEntity);
        if (localPathEntity != null)
            return a(localPathEntity, paramDouble);
        return false;
    }

    public boolean a(PathEntity paramPathEntity, double paramDouble) {
        if (paramPathEntity == null) {
            this.c = null;
            return false;
        }
        if (!paramPathEntity.a(this.c))
            this.c = paramPathEntity;
        if (this.f)
            n();
        if (this.c.d() == 0)
            return false;

        this.d = paramDouble;
        Vec3D localVec3D = j();
        this.h = this.g;
        this.i.c = localVec3D.c;
        this.i.d = localVec3D.d;
        this.i.e = localVec3D.e;
        return true;
    }

    public PathEntity e() {
        return this.c;
    }

    public void f() {
        this.g += 1;
        if (g())
            return;

        if (l())
            i();

        if (g())
            return;
        Vec3D localVec3D = this.c.a(this.a);
        if (localVec3D == null)
            return;

        ((RemotePlayerEntity) this.a).getControllerMove().a(localVec3D.c,
                localVec3D.d, localVec3D.e, this.d);
    }

    private void i() {
        Vec3D localVec3D = j();

        int n = this.c.d();
        for (int i1 = this.c.e(); i1 < this.c.d(); i1++) {
            if (this.c.a(i1).b != (int) localVec3D.d) {
                n = i1;
                break;
            }

        }

        float f1 = this.a.width * this.a.width;
        for (int i2 = this.c.e(); i2 < n; i2++) {
            if (localVec3D.distanceSquared(this.c.a(this.a, i2)) < f1) {
                this.c.c(i2 + 1);
            }

        }

        int i2 = MathHelper.f(this.a.width);
        int i3 = (int) this.a.length + 1;
        int i4 = i2;
        for (int i5 = n - 1; i5 >= this.c.e(); i5--) {
            if (a(localVec3D, this.c.a(this.a, i5), i2, i3, i4)) {
                this.c.c(i5);
                break;
            }

        }

        if (this.g - this.h > 100) {
            if (localVec3D.distanceSquared(this.i) < 2.25D)
                h();
            this.h = this.g;
            this.i.c = localVec3D.c;
            this.i.d = localVec3D.d;
            this.i.e = localVec3D.e;
        }
    }

    public boolean g() {
        return (this.c == null) || (this.c.b());
    }

    public void h() {
        this.c = null;
    }

    private Vec3D j() {
        return this.b.getVec3DPool().create(this.a.locX, k(), this.a.locZ);
    }

    private int k() {
        if ((!this.a.G()) || (!this.m))
            return (int) (this.a.boundingBox.b + 0.5D);

        int n = (int) this.a.boundingBox.b;
        int i1 = this.b.getTypeId(MathHelper.floor(this.a.locX), n,
                MathHelper.floor(this.a.locZ));
        int i2 = 0;
        while ((i1 == Block.WATER.id) || (i1 == Block.STATIONARY_WATER.id)) {
            n++;
            i1 = this.b.getTypeId(MathHelper.floor(this.a.locX), n,
                    MathHelper.floor(this.a.locZ));
            i2++;
            if (i2 > 16)
                return (int) this.a.boundingBox.b;
        }
        return n;
    }

    private boolean l() {
        return (this.a.onGround) || ((this.m) && (m()));
    }

    private boolean m() {
        return (this.a.G()) || (this.a.I());
    }

    private void n() {
        if (this.b.l(MathHelper.floor(this.a.locX),
                (int) (this.a.boundingBox.b + 0.5D),
                MathHelper.floor(this.a.locZ)))
            return;

        for (int n = 0; n < this.c.d(); n++) {
            PathPoint localPathPoint = this.c.a(n);
            if (this.b.l(localPathPoint.a, localPathPoint.b, localPathPoint.c)) {
                this.c.b(n - 1);
                return;
            }
        }
    }

    private boolean a(Vec3D paramVec3D1, Vec3D paramVec3D2, int paramInt1,
            int paramInt2, int paramInt3) {
        int n = MathHelper.floor(paramVec3D1.c);
        int i1 = MathHelper.floor(paramVec3D1.e);

        double d1 = paramVec3D2.c - paramVec3D1.c;
        double d2 = paramVec3D2.e - paramVec3D1.e;
        double d3 = d1 * d1 + d2 * d2;
        if (d3 < 1.0E-008D)
            return false;

        double d4 = 1.0D / Math.sqrt(d3);
        d1 *= d4;
        d2 *= d4;

        paramInt1 += 2;
        paramInt3 += 2;
        if (!a(n, (int) paramVec3D1.d, i1, paramInt1, paramInt2, paramInt3,
                paramVec3D1, d1, d2))
            return false;
        paramInt1 -= 2;
        paramInt3 -= 2;

        double d5 = 1.0D / Math.abs(d1);
        double d6 = 1.0D / Math.abs(d2);

        double d7 = n * 1 - paramVec3D1.c;
        double d8 = i1 * 1 - paramVec3D1.e;
        if (d1 >= 0.0D)
            d7 += 1.0D;
        if (d2 >= 0.0D)
            d8 += 1.0D;
        d7 /= d1;
        d8 /= d2;

        int i2 = d1 < 0.0D ? -1 : 1;
        int i3 = d2 < 0.0D ? -1 : 1;
        int i4 = MathHelper.floor(paramVec3D2.c);
        int i5 = MathHelper.floor(paramVec3D2.e);
        int i6 = i4 - n;
        int i7 = i5 - i1;
        while ((i6 * i2 > 0) || (i7 * i3 > 0)) {
            if (d7 < d8) {
                d7 += d5;
                n += i2;
                i6 = i4 - n;
            } else {
                d8 += d6;
                i1 += i3;
                i7 = i5 - i1;
            }

            if (!a(n, (int) paramVec3D1.d, i1, paramInt1, paramInt2, paramInt3,
                    paramVec3D1, d1, d2))
                return false;
        }
        return true;
    }

    private boolean a(int paramInt1, int paramInt2, int paramInt3,
            int paramInt4, int paramInt5, int paramInt6, Vec3D paramVec3D,
            double paramDouble1, double paramDouble2) {
        int n = paramInt1 - paramInt4 / 2;
        int i1 = paramInt3 - paramInt6 / 2;

        if (!b(n, paramInt2, i1, paramInt4, paramInt5, paramInt6, paramVec3D,
                paramDouble1, paramDouble2))
            return false;

        for (int i2 = n; i2 < n + paramInt4; i2++) {
            for (int i3 = i1; i3 < i1 + paramInt6; i3++) {
                double d1 = i2 + 0.5D - paramVec3D.c;
                double d2 = i3 + 0.5D - paramVec3D.e;
                if (d1 * paramDouble1 + d2 * paramDouble2 >= 0.0D) {
                    int i4 = this.b.getTypeId(i2, paramInt2 - 1, i3);
                    if (i4 <= 0)
                        return false;
                    Material localMaterial = Block.byId[i4].material;
                    if ((localMaterial == Material.WATER) && (!this.a.G()))
                        return false;
                    if (localMaterial == Material.LAVA)
                        return false;
                }
            }
        }
        return true;
    }

    private boolean b(int paramInt1, int paramInt2, int paramInt3,
            int paramInt4, int paramInt5, int paramInt6, Vec3D paramVec3D,
            double paramDouble1, double paramDouble2) {
        for (int n = paramInt1; n < paramInt1 + paramInt4; n++) {
            for (int i1 = paramInt2; i1 < paramInt2 + paramInt5; i1++)
                for (int i2 = paramInt3; i2 < paramInt3 + paramInt6; i2++) {
                    double d1 = n + 0.5D - paramVec3D.c;
                    double d2 = i2 + 0.5D - paramVec3D.e;
                    if (d1 * paramDouble1 + d2 * paramDouble2 >= 0.0D) {
                        int i3 = this.b.getTypeId(n, i1, i2);
                        if(Block.byId[i3] != null) {
                        Block.byId[i3].b(this.b, n, i1, i2);
                        if ((i3 > 0)
                                && (Block.byId[i3].b(this.b, n, i1, i2) == null))
                            return false;
                        }
                    }
                }
        }
        return true;
    }

}