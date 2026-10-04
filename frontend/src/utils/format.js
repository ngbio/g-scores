export const formatCount = (value) =>
  new Intl.NumberFormat("vi-VN").format(value);
export const formatScore = (value) =>
  value == null
    ? ""
    : new Intl.NumberFormat("vi-VN", { maximumFractionDigits: 2 }).format(
        value,
      );
