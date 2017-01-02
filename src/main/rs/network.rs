#pragma version(1)
#pragma rs java_package_name(org.furidamu.androidgo)
#pragma rs_fp_relaxed

// rs_allocation matrixA;
// rs_allocation matrixB;
// rs_allocation numOps;
// uint32_t m;

void mmul(const float* in, float* out, uint32_t x, uint32_t y) {
  rsDebug("executing: ", x, y);
  // rsSetElementAt_float(numOps, 1, x, y);
  // float sum = 0f;
    // for (uint32_t i = 0; i < m; ++i) {
    //     float a = rsGetElementAt_float(matrixA, x, i);
    //     float b = rsGetElementAt_float(matrixB, i, y);
    //     sum += a * b;
    // }
  *out = 1;
}
