#pragma version(1)
#pragma rs java_package_name(org.furidamu.androidgo)

#pragma rs reduce(addint) accumulator(addintAccum)

static void addintAccum(int *accum, int val) {
  *accum += val;
}
