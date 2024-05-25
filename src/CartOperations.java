import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CartOperations {
    private static final String baseDir = "backend/_build/default/bin/";

    public static float getTotalCartPrice(ShoppingCart cart) {
        float totalCartPrice = 0;

        StringBuilder args = new StringBuilder();
        for(var item: cart.getItems()) {
            int quantity = item.getQuantity();
            int id = item.getId();

            args.append((id + " ").repeat(Math.max(0, quantity)));
        }
        try {
            Process process = Runtime.getRuntime()
                    .exec(baseDir + "totalPrice.exe " + args.toString());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line = reader.readLine();

            if (line != null) {
                totalCartPrice = Float.parseFloat(line.trim());
            }

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return totalCartPrice;
    }

    public static float getCategoryDiscount(ShoppingCart cart) {
        float categoryDiscount = 0;

        StringBuilder args = new StringBuilder();
        for(var item: cart.getItems()) {
            int quantity = item.getQuantity();
            int id = item.getId();

            args.append((id + " ").repeat(Math.max(0, quantity)));
        }

        try {
            Process process = Runtime.getRuntime()
                    .exec(baseDir + "categoryDiscount.exe " + args.toString());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line = reader.readLine();

            if (line != null) {
                categoryDiscount = Float.parseFloat(line.trim());
            }

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return categoryDiscount;
    }

    public static float getLoyaltyDiscountPrice(int loyaltyYears, float totalPrice) {
        float loyaltyDiscount = 0;

        String arg = String.valueOf(loyaltyYears) + " " + String.valueOf(totalPrice);

        try {
            Process process = Runtime.getRuntime()
                    .exec(baseDir + "loyaltyDiscount.exe " + arg);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line = reader.readLine();

            if (line != null) {
                loyaltyDiscount = Float.parseFloat(line.trim());
            }

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        return loyaltyDiscount;
    }

    public static float getShippingCost(String distric) {
        float shippingCost = 0;

        try {
            Process process = Runtime.getRuntime()
                    .exec(baseDir + "shippingCost.exe " + distric);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line = reader.readLine();

            if (line != null) {
                shippingCost = Float.parseFloat(line.trim());
            }

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        return shippingCost;
    }

    public static float getFinalPrice(float totalPrice, float categoryDiscount, float loyaltyDiscount, float shippingCost) {
        float finalPrice = 0;

        String args =
                totalPrice + " " +
                categoryDiscount + " " +
                loyaltyDiscount + " " + shippingCost;

        try {
            Process process = Runtime.getRuntime()
                    .exec(baseDir + "finalPrice.exe " + args);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line = reader.readLine();

            if (line != null) {
                finalPrice = Float.parseFloat(line.trim());
            }

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        return finalPrice;
    }
}
