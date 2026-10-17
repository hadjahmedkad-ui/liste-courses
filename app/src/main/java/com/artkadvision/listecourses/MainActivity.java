package com.artkadvision.listecourses;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.*;
import android.graphics.pdf.PdfDocument;
import android.print.*;
import android.os.ParcelFileDescriptor;
import android.text.*;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity {

    final String[] categories = {
        "Fruits", "Légumes", "Viandes et volailles",
        "Poissons et fruits de mer", "Épicerie et pâtes",
        "Conserves", "Produits frais", "Surgelés et glaces",
        "Cuisine et entretien", "Lessive", "Toilettes et douche",
        "Hygiène féminine", "Vêtements", "Bébé", "Boissons",
        "Boulangerie et pâtisserie", "Animaux", "Autres"
    };

    final String[][] products = {
        {
            "🍎|Pommes|kg", "🍌|Bananes|kg",
            "🍊|Oranges|kg", "🍐|Poires|kg",
            "🍇|Raisins|kg", "🍓|Fraises|kg",
            "🍒|Cerises|kg", "🍑|Pêches|kg",
            "🍑|Abricots|kg", "🍋|Citrons|kg",
            "🍉|Pastèque|kg", "🍈|Melon|pièce",
            "🥝|Kiwis|kg", "🍍|Ananas|pièce",
            "🥭|Mangues|pièce", "🫐|Myrtilles|barquette",
            "🍊|Clémentines|kg", "🍎|Compote|pot",
            "🌴|Dattes|kg", "🥥|Noix de coco|pièce"
        },
        {
            "🥕|Carottes|kg", "🥔|Pommes de terre|kg",
            "🍅|Tomates|kg", "🥬|Salade|pièce",
            "🧅|Oignons|kg", "🧄|Ail|pièce",
            "🥒|Concombres|pièce", "🥒|Courgettes|kg",
            "🍆|Aubergines|kg", "🫑|Poivrons|kg",
            "🥦|Brocolis|kg", "🥬|Chou|pièce",
            "🥬|Épinards|kg", "🫘|Haricots verts|kg",
            "🍄|Champignons|kg", "🥕|Navets|kg",
            "🥬|Poireaux|botte", "🌿|Persil|botte",
            "🌿|Coriandre|botte", "🌶️|Piments|kg",
            "🥬|Céleri|botte", "🎃|Courge|kg",
            "🌽|Maïs|pièce"
        },
        {
            "🍗|Poulet entier|kg", "🍗|Cuisses de poulet|kg",
            "🍗|Blanc de poulet|kg", "🥩|Bœuf|kg",
            "🥩|Viande hachée|kg", "🍖|Agneau|kg",
            "🍖|Mouton|kg", "🥩|Veau|kg",
            "🦃|Dinde|kg", "🍔|Steaks hachés|boîte",
            "🌭|Saucisses|kg", "🥓|Charcuterie|paquet",
            "🥚|Œufs|pièce", "🍗|Nuggets|boîte",
            "🍖|Brochettes|kg"
        },
        {
            "🐟|Poisson frais|kg", "🐟|Sardines|kg",
            "🐟|Saumon|kg", "🐟|Cabillaud|kg",
            "🐟|Merlu|kg", "🐟|Dorade|kg",
            "🐟|Thon frais|kg", "🐠|Filets de poisson|kg",
            "🦐|Crevettes|kg", "🦑|Calamars|kg",
            "🦪|Moules|kg", "🦀|Crabe|kg",
            "🐙|Poulpe|kg", "🐟|Poisson pané|boîte"
        },
        {
            "🍝|Pâtes|paquet", "🍚|Riz|kg",
            "🥣|Semoule|kg", "🥣|Couscous|kg",
            "🥣|Farine|kg", "🧂|Sel|paquet",
            "🍬|Sucre|kg", "🫒|Huile d'olive|L",
            "🫗|Huile de tournesol|L", "🧴|Vinaigre|L",
            "🍯|Miel|pot", "🍓|Confiture|pot",
            "🥜|Beurre de cacahuète|pot",
            "🍫|Chocolat|tablette", "🍪|Biscuits|paquet",
            "☕|Café|paquet", "🍵|Thé|boîte",
            "🧂|Poivre|boîte", "🌶️|Épices|boîte",
            "🥣|Lentilles|kg", "🫘|Pois chiches|kg",
            "🫘|Haricots secs|kg", "🥣|Boulgour|kg",
            "🥣|Flocons d'avoine|paquet",
            "🥣|Céréales|boîte", "🍅|Sauce tomate|pot",
            "🥫|Mayonnaise|pot", "🥫|Moutarde|pot",
            "🍅|Ketchup|flacon", "🍜|Nouilles|paquet"
        },
        {
            "🥫|Tomates en conserve|boîte",
            "🥫|Thon|boîte", "🥫|Sardines|boîte",
            "🥫|Maquereaux|boîte", "🥫|Petits pois|boîte",
            "🥫|Haricots|boîte", "🥫|Maïs|boîte",
            "🥫|Champignons|boîte",
            "🥫|Pois chiches|boîte", "🥫|Lentilles|boîte",
            "🥫|Olives|bocal", "🥫|Cornichons|bocal",
            "🥫|Soupe|boîte",
            "🥫|Concentré de tomate|boîte",
            "🥫|Fruits au sirop|boîte"
        },
        {
            "🥛|Lait|L", "🧀|Fromage|pièce",
            "🧀|Fromage râpé|paquet",
            "🧀|Fromage blanc|pot", "🧈|Beurre|pièce",
            "🥛|Yaourts|pièce", "🥛|Crème fraîche|pot",
            "🥛|Crème liquide|L",
            "🥛|Desserts lactés|pièce",
            "🥚|Œufs|boîte",
            "🥗|Salades préparées|barquette",
            "🥟|Pâte feuilletée|pièce",
            "🥟|Pâte brisée|pièce",
            "🥛|Lait fermenté|L",
            "🥛|Mozzarella|pièce"
        },
        {
            "🍦|Glaces|boîte", "🍧|Sorbet|boîte",
            "🧊|Légumes surgelés|kg",
            "🍕|Pizza surgelée|pièce",
            "🍟|Frites surgelées|kg",
            "🐟|Poisson surgelé|kg",
            "🍗|Poulet surgelé|kg",
            "🥦|Brocolis surgelés|kg",
            "🥬|Épinards surgelés|kg",
            "🥘|Plats préparés|boîte",
            "🥐|Viennoiseries surgelées|boîte",
            "🍓|Fruits surgelés|kg"
        },
        {
            "🧽|Éponges|pièce",
            "🧴|Liquide vaisselle|L",
            "🧻|Essuie-tout|rouleau",
            "🧹|Nettoyant cuisine|L",
            "🧴|Nettoyant sol|L",
            "🧴|Nettoyant vitres|L",
            "🧴|Dégraissant|L",
            "🧴|Eau de Javel|L",
            "🧻|Sacs poubelle|rouleau",
            "🧤|Gants ménagers|paire",
            "🧹|Balai|pièce", "🪣|Seau|pièce",
            "🧽|Serpillière|pièce",
            "🧻|Film alimentaire|rouleau",
            "🧻|Papier aluminium|rouleau",
            "🧻|Papier cuisson|rouleau",
            "🕯️|Bougies|paquet",
            "🔋|Piles|paquet", "💡|Ampoules|pièce"
        },
        {
            "🧺|Lessive liquide|L",
            "🧺|Lessive poudre|kg",
            "🧺|Capsules de lessive|boîte",
            "🧴|Adoucissant|L",
            "🧼|Détachant|pièce",
            "🧴|Anti-calcaire|L",
            "🧺|Lingettes anti-décoloration|boîte",
            "🧺|Désinfectant linge|L"
        },
        {
            "🧴|Shampooing|L",
            "🧴|Après-shampooing|L",
            "🧼|Savon|pièce",
            "🚿|Gel douche|L",
            "🪥|Dentifrice|pièce",
            "🪥|Brosses à dents|pièce",
            "🧻|Papier toilette|rouleau",
            "🧴|Déodorant|pièce",
            "🪒|Rasoirs|paquet",
            "🧴|Mousse à raser|pièce",
            "🧴|Crème hydratante|pièce",
            "🧴|Parfum|pièce",
            "🧻|Mouchoirs|boîte",
            "🧻|Cotons|paquet",
            "🧼|Gel mains|L",
            "🪮|Peigne|pièce",
            "🪥|Bain de bouche|L",
            "🧴|Crème solaire|pièce"
        },
        {
            "🌸|Serviettes hygiéniques|paquet",
            "🌸|Tampons|boîte",
            "🌸|Protège-slips|paquet",
            "🧴|Soin féminin|pièce",
            "🧴|Gel intime|pièce",
            "🧴|Crème visage|pièce",
            "💄|Maquillage|pièce",
            "🧴|Démaquillant|pièce",
            "🌸|Culottes menstruelles|pièce"
        },
        {
            "👕|T-shirt|pièce",
            "👖|Pantalon|pièce",
            "🧦|Chaussettes|paire",
            "👟|Chaussures|paire",
            "👔|Chemise|pièce",
            "🧥|Veste|pièce",
            "🧥|Manteau|pièce",
            "👗|Robe|pièce",
            "👚|Pull|pièce",
            "🩳|Short|pièce",
            "🩲|Sous-vêtements|pièce",
            "🧢|Casquette|pièce",
            "🧣|Écharpe|pièce",
            "🧤|Gants|paire",
            "👞|Chaussons|paire",
            "👜|Sac|pièce"
        },
        {
            "🍼|Lait infantile|boîte",
            "🍼|Biberon|pièce",
            "👶|Couches|paquet",
            "🧻|Lingettes bébé|paquet",
            "🧴|Gel lavant bébé|pièce",
            "🧴|Crème bébé|pièce",
            "🥣|Petits pots|pièce",
            "🥣|Céréales bébé|boîte",
            "👶|Tétine|pièce",
            "👕|Vêtements bébé|pièce"
        },
        {
            "💧|Eau minérale|L",
            "💧|Eau gazeuse|L",
            "🧃|Jus d'orange|L",
            "🧃|Jus de fruits|L",
            "🥤|Soda|L",
            "🥤|Limonade|L",
            "🍵|Thé glacé|L",
            "🥛|Boisson végétale|L",
            "☕|Café soluble|pot",
            "🍫|Chocolat en poudre|boîte",
            "🧃|Sirop|L"
        },
        {
            "🥖|Baguette|pièce",
            "🍞|Pain|pièce",
            "🍞|Pain de mie|paquet",
            "🥐|Croissant|pièce",
            "🥐|Pain au chocolat|pièce",
            "🥯|Brioche|pièce",
            "🥨|Biscottes|paquet",
            "🥞|Crêpes|paquet",
            "🥧|Tarte|pièce",
            "🎂|Gâteau|pièce",
            "🍪|Cookies|paquet",
            "🥯|Pain burger|paquet",
            "🫓|Pain pita|paquet"
        },
        {
            "🐈|Croquettes chat|kg",
            "🐕|Croquettes chien|kg",
            "🥫|Pâtée chat|boîte",
            "🥫|Pâtée chien|boîte",
            "🐈|Litière|kg",
            "🐕|Friandises animaux|paquet",
            "🧴|Shampooing animaux|pièce",
            "🧸|Jouet animaux|pièce"
        },
        {
            "🛍️|Autre produit|pièce"
        }
    };

    final String[] stores = {
        "Carrefour", "Carrefour Market", "Carrefour City",
        "E.Leclerc", "Lidl", "Aldi", "Auchan",
        "Intermarché", "Super U", "Hyper U", "Casino",
        "Monoprix", "Franprix", "Netto", "Spar", "Vival",
        "Grand Frais", "Action", "Normal", "GiFi", "ABO",
        "Boucherie", "Boucherie halal", "Épicerie",
        "Épicerie orientale", "Épicerie de quartier",
        "Primeur (fruits et légumes)", "Poissonnerie",
        "Boulangerie", "Pâtisserie", "Fromagerie",
        "Charcuterie", "Rôtisserie", "Marché",
        "Marchand ambulant", "Magasin bio",
        "Surgelés (Picard)", "Pharmacie",
        "Parapharmacie", "Droguerie",
        "Magasin de vêtements",
        "Magasin de chaussures",
        "Bazaar / Bazar", "Autre magasin"
    };

    final String[] units = {
        "pièce", "kg", "g", "L", "mL", "paquet",
        "boîte", "rouleau", "paire", "pot",
        "bocal", "barquette", "botte",
        "flacon", "tablette"
    };

    final ArrayList<Item> items = new ArrayList<>();
    final ArrayList<String> history = new ArrayList<>();
    final ArrayList<String> customStores = new ArrayList<>();

    final String[] payers = {
        "Madame", "Monsieur", "Mr et Mme"
    };

    final double[] card = {0, 0, 0};
    final double[] cash = {0, 0, 0};

    LinearLayout root, body;
    TextView summary;
    String tab = "catalogue";
    String date;
    int installments = 1;

    static class Item {
        String name, unit, emoji, store = "";
        double qty, price;
        boolean bought;

        Item(String n, String u, String e,
             double q, double p, boolean b) {
            name = n;
            unit = u;
            emoji = e;
            qty = q;
            price = p;
            bought = b;
        }
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        date = new SimpleDateFormat(
            "dd/MM/yyyy", Locale.FRANCE
        ).format(new Date());
        load();
        show();
    }

    int dp(int n) {
        return (int) (
            n * getResources().getDisplayMetrics().density + .5f
        );
    }

    LinearLayout vertical() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(1);
        return l;
    }

    TextView text(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(0xff25372d);
        t.setPadding(dp(5), dp(7), dp(5), dp(7));
        return t;
    }

    Button button(String s) {
        Button b = new Button(this);
        b.setText(s);
        b.setAllCaps(false);
        return b;
    }

    EditText field(String hint, String value, boolean numeric) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setHint(hint);
        if (numeric) {
            e.setInputType(
                InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | InputType.TYPE_NUMBER_FLAG_SIGNED
            );
        }
        e.setText(value);
        return e;
    }

    void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    double parse(String s) {
        try {
            double d = Double.parseDouble(
                s.trim().replace(',', '.')
            );
            return Double.isFinite(d) ? d : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    String fmt(double d) {
        return String.format(Locale.FRANCE, "%.2f", d);
    }

    TextWatcher watch(Runnable r) {
        return new TextWatcher() {
            public void beforeTextChanged(
                CharSequence s, int a, int c, int f
            ) {}

            public void onTextChanged(
                CharSequence s, int a, int b, int c
            ) {
                r.run();
            }

            public void afterTextChanged(Editable e) {}
        };
    }

    String[] allStores() {
        ArrayList<String> a = new ArrayList<>(
            Arrays.asList(stores)
        );

        for (String s : customStores) {
            if (!a.contains(s)) a.add(s);
        }

        return a.toArray(new String[0]);
    }

    void addStoreDialog(Runnable after) {
        EditText input = field("Nom du magasin", "", false);

        new AlertDialog.Builder(this)
            .setTitle("Nouveau magasin")
            .setView(input)
            .setPositiveButton("Ajouter", (d, w) -> {
                String n = input.getText().toString().trim();

                if (n.isEmpty()) {
                    toast("Nom vide");
                    return;
                }

                if (!Arrays.asList(allStores()).contains(n)) {
                    customStores.add(n);
                }

                save();
                after.run();
            })
            .setNegativeButton("Annuler", null)
            .show();
    }

    Spinner picker(
        String value,
        java.util.function.Consumer<String> callback
    ) {
        Spinner sp = new Spinner(this);
        String[] available = allStores();
        String[] opts = new String[available.length + 1];

        opts[0] = "Choisir un magasin";
        System.arraycopy(
            available, 0, opts, 1, available.length
        );

        sp.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            opts
        ));

        int ix = Arrays.asList(opts).indexOf(value);
        sp.setSelection(ix < 0 ? 0 : ix);

        sp.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                boolean initial = true;

                public void onNothingSelected(
                    AdapterView<?> a
                ) {}

                public void onItemSelected(
                    AdapterView<?> a,
                    View v,
                    int pos,
                    long id
                ) {
                    if (initial) {
                        initial = false;
                        return;
                    }

                    if (pos > 0) callback.accept(opts[pos]);
                }
            }
        );

        return sp;
    }

    void show() {
        ScrollView scroll = new ScrollView(this);
        root = vertical();
        root.setPadding(
            dp(8), dp(8), dp(8), dp(20)
        );
        root.setBackgroundColor(0xfff9faf6);

        scroll.addView(root);
        setContentView(scroll);

        TextView title = text("🛒 Liste Courses", 25);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        LinearLayout tabs = vertical();
        root.addView(tabs);

        String[][] choices = {
            {"catalogue", "📋 Ma liste"},
            {"achats", "🧮 Achats"},
            {"magasins", "🏬 Magasins"},
            {"historique", "🗂 Historique"}
        };

        for (int x = 0; x < choices.length; x += 2) {
            LinearLayout line = new LinearLayout(this);
            tabs.addView(line);

            for (
                int k = x;
                k < Math.min(x + 2, choices.length);
                k++
            ) {
                final String target = choices[k][0];
                Button b = button(choices[k][1]);

                line.addView(
                    b,
                    new LinearLayout.LayoutParams(
                        0, dp(52), 1
                    )
                );

                b.setEnabled(!tab.equals(target));

                b.setOnClickListener(v -> {
                    tab = target;
                    show();
                });
            }
        }

        body = vertical();
        root.addView(body);

        if (tab.equals("catalogue")) {
            catalogue();
        } else if (tab.equals("achats")) {
            achats();
        } else if (tab.equals("magasins")) {
            magasins();
        } else {
            historique();
        }
    }

    void addItem(
        String n, String u, String emoji, double q
    ) {
        items.add(new Item(n, u, emoji, q, 0, false));
        save();
        toast(n + " ajouté");
    }

    void catalogue() {
        body.addView(text(
            "Choisis une catégorie et coche les produits.",
            16
        ));

        Spinner categoriesSpinner = new Spinner(this);

        categoriesSpinner.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        ));

        body.addView(categoriesSpinner);

        LinearLayout list = vertical();
        body.addView(list);

        categoriesSpinner.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onNothingSelected(
                    AdapterView<?> p
                ) {}

                public void onItemSelected(
                    AdapterView<?> p,
                    View v,
                    int pos,
                    long id
                ) {
                    list.removeAllViews();

                    for (String raw : products[pos]) {
                        String[] a = raw.split("\\|");

                        if (a.length == 3) {
                            productRow(
                                list, a[1], a[2], a[0]
                            );
                        }
                    }
                }
            }
        );

        body.addView(text(
            "Ajouter un produit personnel", 19
        ));

        EditText name = field(
            "Nom du produit", "", false
        );
        body.addView(name);

        Spinner unit = new Spinner(this);
        unit.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            units
        ));
        body.addView(unit);

        EditText quantity = field(
            "Quantité", "1", true
        );
        body.addView(quantity);

        Button add = button("+ Ajouter à ma liste");
        body.addView(add);

        add.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            double q = parse(
                quantity.getText().toString()
            );

            if (n.isEmpty() || q <= 0) {
                toast("Nom ou quantité invalide");
                return;
            }

            addItem(
                n,
                unit.getSelectedItem().toString(),
                "🛍️",
                q
            );

            name.setText("");
            show();
        });

        body.addView(text(
            "Ma liste préparée : "
            + items.size()
            + " ligne(s)",
            19
        ));

        for (int i = 0; i < items.size(); i++) {
            final int ix = i;
            Item it = items.get(i);

            LinearLayout row = new LinearLayout(this);
            body.addView(row);

            row.addView(
                text(
                    it.emoji + " " + it.name
                    + " — " + fmt(it.qty)
                    + " " + it.unit,
                    14
                ),
                new LinearLayout.LayoutParams(
                    0, -2, 1
                )
            );

            Button del = button("✕");
            row.addView(del);

            del.setOnClickListener(v -> {
                items.remove(ix);
                save();
                show();
            });
        }
    }

    void productRow(
        LinearLayout parent,
        String n,
        String u,
        String emoji
    ) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        parent.addView(row);

        CheckBox check = new CheckBox(this);
        check.setText(emoji + " " + n);

        row.addView(
            check,
            new LinearLayout.LayoutParams(
                0, dp(55), 1
            )
        );

        EditText qty = field("Qté", "1", true);

        row.addView(
            qty,
            new LinearLayout.LayoutParams(
                dp(60), dp(52)
            )
        );

        row.addView(text(u, 11));

        check.setOnCheckedChangeListener((b, c) -> {
            if (c) {
                double q = parse(
                    qty.getText().toString()
                );

                if (q <= 0) {
                    check.setChecked(false);
                    toast("Quantité invalide");
                    return;
                }

                addItem(n, u, emoji, q);
                check.setChecked(false);
            }
        });
    }

    void achats() {
        body.addView(text("Date : " + date, 17));

        Button change = button("📅 Changer la date");
        body.addView(change);

        change.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    date = String.format(
                        Locale.FRANCE,
                        "%02d/%02d/%04d",
                        d, m + 1, y
                    );

                    save();
                    show();
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        summary = text("", 17);
        body.addView(summary);

        Button addStore = button(
            "+ Ajouter un magasin"
        );
        body.addView(addStore);

        addStore.setOnClickListener(
            v -> addStoreDialog(() -> show())
        );

        if (items.isEmpty()) {
            body.addView(text(
                "Ta liste est vide.", 16
            ));
        }

        for (int i = 0; i < items.size(); i++) {
            purchaseRow(i);
        }

        Button scan = button(
            "📷 Scanner un code-barres"
        );
        body.addView(scan);

        scan.setOnClickListener(v -> {
            IntentIntegrator it =
                new IntentIntegrator(this);

            it.setPrompt("Scanner le code-barres");
            it.setBeepEnabled(false);
            it.initiateScan();
        });

        body.addView(text(
            "Saisie libre d'un achat", 19
        ));

        EditText name = field(
            "Produit", "", false
        );
        EditText q = field(
            "Quantité", "1", true
        );
        EditText price = field(
            "Prix unitaire (€)", "", true
        );

        body.addView(name);
        body.addView(q);
        body.addView(price);

        Button add = button(
            "+ Enregistrer cet achat"
        );
        body.addView(add);

        add.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            double quantity = parse(
                q.getText().toString()
            );
            double p = parse(
                price.getText().toString()
            );

            if (
                n.isEmpty()
                || quantity <= 0
                || p < 0
            ) {
                toast("Saisie invalide");
                return;
            }

            items.add(new Item(
                n, "pièce", "🛍️",
                quantity, p, true
            ));

            save();
            show();
        });

        body.addView(text(
            "Paiements : montant et moyen pour chaque payeur",
            19
        ));

        for (int i = 0; i < 3; i++) {
            payerRow(i);
        }

        body.addView(text(
            "Nombre de paiements (1 = en une fois)",
            16
        ));

        Spinner split = new Spinner(this);

        String[] counts = {
            "1", "2", "3", "4",
            "5", "6", "10", "12"
        };

        split.setAdapter(new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            counts
        ));

        for (int i = 0; i < counts.length; i++) {
            if (
                Integer.parseInt(counts[i])
                == installments
            ) {
                split.setSelection(i);
            }
        }

        body.addView(split);

        split.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onNothingSelected(
                    AdapterView<?> p
                ) {}

                public void onItemSelected(
                    AdapterView<?> p,
                    View v,
                    int pos,
                    long id
                ) {
                    installments = Integer.parseInt(
                        counts[pos]
                    );

                    save();
                    refreshSummary();
                }
            }
        );

        Button print = button(
            "🖨️ Imprimer la liste complète des achats"
        );
        body.addView(print);

        print.setOnClickListener(v ->
            printText(
                ticket(),
                "Liste Courses " + date
            )
        );

        Button finish = button(
            "✅ Enregistrer les courses dans l'historique"
        );
        body.addView(finish);

        finish.setOnClickListener(v -> {
            if (boughtCount() == 0) {
                toast("Aucun achat coché");
                return;
            }

            if (
                Math.abs(total() - paidTotal())
                > 0.011
            ) {
                new AlertDialog.Builder(this)
                    .setTitle("Montants à vérifier")
                    .setMessage(
                        "Total achats : "
                        + fmt(total()) + " €\n"
                        + "Total paiements : "
                        + fmt(paidTotal()) + " €\n"
                        + "Écart : "
                        + fmt(total() - paidTotal())
                        + " €\n"
                        + "Enregistrer quand même ?"
                    )
                    .setPositiveButton(
                        "Oui",
                        (d, w) -> confirmFinish()
                    )
                    .setNegativeButton(
                        "Non", null
                    )
                    .show();
            } else {
                confirmFinish();
            }
        });

        refreshSummary();
    }

    void purchaseRow(int index) {
        Item it = items.get(index);

        LinearLayout box = vertical();
        box.setPadding(
            dp(3), dp(5), dp(3), dp(5)
        );
        body.addView(box);

        CheckBox check = new CheckBox(this);
        check.setText(it.emoji + " " + it.name);
        check.setChecked(it.bought);
        box.addView(check);

        check.setOnCheckedChangeListener(
            (b, c) -> {
                it.bought = c;
                save();
                refreshSummary();
            }
        );

        LinearLayout storeRow =
            new LinearLayout(this);

        storeRow.setGravity(
            Gravity.CENTER_VERTICAL
        );
        box.addView(storeRow);

        storeRow.addView(
            text("Magasin", 13)
        );

        Spinner sp = picker(
            it.store,
            s -> {
                if (s.equals("Autre magasin")) {
                    addStoreDialog(() -> show());
                } else {
                    it.store = s;
                    save();
                    refreshSummary();
                }
            }
        );

        storeRow.addView(
            sp,
            new LinearLayout.LayoutParams(
                0, dp(53), 1
            )
        );

        Button plus = button("+");
        storeRow.addView(plus);

        plus.setOnClickListener(
            v -> addStoreDialog(() -> show())
        );

        LinearLayout line = new LinearLayout(this);
        line.setGravity(Gravity.CENTER_VERTICAL);
        box.addView(line);

        EditText qty = field(
            "Qté", fmt(it.qty), true
        );

        line.addView(
            qty,
            new LinearLayout.LayoutParams(
                0, dp(53), 1
            )
        );

        line.addView(text(it.unit, 12));

        EditText price = field(
            "Prix €",
            it.price == 0 ? "" : fmt(it.price),
            true
        );

        line.addView(
            price,
            new LinearLayout.LayoutParams(
                0, dp(53), 1
            )
        );

        Button del = button("✕");
        line.addView(del);

        del.setOnClickListener(v -> {
            items.remove(index);
            save();
            show();
        });

        qty.addTextChangedListener(
            watch(() -> {
                double n = parse(
                    qty.getText().toString()
                );

                if (n > 0) {
                    it.qty = n;
                    save();
                    refreshSummary();
                }
            })
        );

        price.addTextChangedListener(
            watch(() -> {
                double n = parse(
                    price.getText().toString()
                );

                if (n >= 0) {
                    it.price = n;
                    save();
                    refreshSummary();
                }
            })
        );
    }

    void payerRow(int i) {
        body.addView(text(payers[i], 17));

        LinearLayout row = new LinearLayout(this);
        body.addView(row);

        EditText ce = field(
            "Carte (€)",
            card[i] == 0 ? "" : fmt(card[i]),
            true
        );

        EditText ca = field(
            "Espèces (€)",
            cash[i] == 0 ? "" : fmt(cash[i]),
            true
        );

        row.addView(
            ce,
            new LinearLayout.LayoutParams(
                0, dp(55), 1
            )
        );

        row.addView(
            ca,
            new LinearLayout.LayoutParams(
                0, dp(55), 1
            )
        );

        ce.addTextChangedListener(
            watch(() -> {
                card[i] = Math.max(
                    0,
                    parse(ce.getText().toString())
                );

                save();
                refreshSummary();
            })
        );

        ca.addTextChangedListener(
            watch(() -> {
                cash[i] = Math.max(
                    0,
                    parse(ca.getText().toString())
                );

                save();
                refreshSummary();
            })
        );
    }

    double total() {
        double sum = 0;

        for (Item it : items) {
            if (it.bought) {
                sum += it.qty * it.price;
            }
        }

        return sum;
    }

    int boughtCount() {
        int n = 0;

        for (Item it : items) {
            if (it.bought) n++;
        }

        return n;
    }

    double paidTotal() {
        double n = 0;

        for (int i = 0; i < 3; i++) {
            n += card[i] + cash[i];
        }

        return n;
    }

    void refreshSummary() {
        if (
            summary == null
            || !tab.equals("achats")
        ) {
            return;
        }

        double unitsCount = 0;

        for (Item it : items) {
            if (it.bought) {
                unitsCount += it.qty;
            }
        }

        summary.setText(
            "Produits achetés : "
            + boughtCount()
            + " lignes | Quantités : "
            + fmt(unitsCount)
            + " (unités variées)\n"
            + "TOTAL : "
            + fmt(total()) + " €\n"
            + "Paiements saisis : "
            + fmt(paidTotal()) + " €\n"
            + "Reste à régler (+) / trop-perçu (-) : "
            + fmt(total() - paidTotal()) + " €\n"
            + installments
            + " paiement(s) : environ "
            + fmt(total() / installments)
            + " € chacun"
        );
    }

    LinkedHashMap<String, ArrayList<Item>> groups() {
        LinkedHashMap<String, ArrayList<Item>> map =
            new LinkedHashMap<>();

        for (Item it : items) {
            if (it.bought) {
                String store = it.store.isEmpty()
                    ? "Magasin non renseigné"
                    : it.store;

                if (!map.containsKey(store)) {
                    map.put(
                        store,
                        new ArrayList<>()
                    );
                }

                map.get(store).add(it);
            }
        }

        return map;
    }

    double subtotal(ArrayList<Item> group) {
        double n = 0;

        for (Item it : group) {
            n += it.qty * it.price;
        }

        return n;
    }

    void magasins() {
        body.addView(text(
            "Récapitulatif par magasin", 22
        ));

        Button add = button(
            "+ Ajouter un magasin"
        );
        body.addView(add);

        add.setOnClickListener(
            v -> addStoreDialog(() -> show())
        );

        LinkedHashMap<String, ArrayList<Item>> map =
            groups();

        if (map.isEmpty()) {
            body.addView(text(
                "Aucun achat enregistré.", 16
            ));
        }

        for (String store : map.keySet()) {
            TextView h = text(
                "🏬 " + store, 20
            );
            h.setTypeface(null, Typeface.BOLD);
            body.addView(h);

            for (Item it : map.get(store)) {
                body.addView(text(
                    it.name
                    + " — " + fmt(it.qty)
                    + " " + it.unit
                    + " × " + fmt(it.price)
                    + " € = "
                    + fmt(it.qty * it.price)
                    + " €",
                    14
                ));
            }

            TextView sum = text(
                "TOTAL " + store + " : "
                + fmt(subtotal(map.get(store)))
                + " €",
                17
            );

            sum.setTypeface(
                null, Typeface.BOLD
            );
            body.addView(sum);

            View rule = new View(this);
            rule.setBackgroundColor(0xffcccccc);

            body.addView(
                rule,
                new LinearLayout.LayoutParams(
                    -1, dp(1)
                )
            );
        }

        body.addView(text(
            "TOTAL GÉNÉRAL : "
            + fmt(total()) + " €",
            22
        ));

        Button print = button(
            "🖨️ Imprimer le récapitulatif"
        );
        body.addView(print);

        print.setOnClickListener(v ->
            printText(
                ticket(),
                "Récapitulatif " + date
            )
        );
    }

    String ticket() {
        StringBuilder s = new StringBuilder(
            "LISTE COMPLÈTE DES ACHATS\n"
            + "Date : " + date + "\n\n"
        );

        LinkedHashMap<String, ArrayList<Item>> map =
            groups();

        for (String store : map.keySet()) {
            ArrayList<Item> group = map.get(store);

            s.append("MAGASIN : ")
                .append(store)
                .append("\n");

            for (Item it : group) {
                s.append("  ")
                    .append(it.name)
                    .append(" | ")
                    .append(fmt(it.qty))
                    .append(" ")
                    .append(it.unit)
                    .append(" x ")
                    .append(fmt(it.price))
                    .append(" € = ")
                    .append(fmt(it.qty * it.price))
                    .append(" €\n");
            }

            s.append("SOUS-TOTAL : ")
                .append(fmt(subtotal(group)))
                .append(" €\n\n");
        }

        s.append("PRODUITS NON ACHETÉS\n");

        for (Item it : items) {
            if (!it.bought) {
                s.append("  ")
                    .append(it.name)
                    .append(" — ")
                    .append(fmt(it.qty))
                    .append(" ")
                    .append(it.unit)
                    .append("\n");
            }
        }

        s.append("\nProduits achetés : ")
            .append(boughtCount())
            .append("\nTOTAL ACHATS : ")
            .append(fmt(total()))
            .append(" €\n\nPAIEMENTS\n");

        for (int i = 0; i < 3; i++) {
            s.append(payers[i])
                .append(" : carte ")
                .append(fmt(card[i]))
                .append(" € ; espèces ")
                .append(fmt(cash[i]))
                .append(" € ; total ")
                .append(fmt(card[i] + cash[i]))
                .append(" €\n");
        }

        s.append("TOTAL PAYÉ : ")
            .append(fmt(paidTotal()))
            .append(" €\nÉCART : ")
            .append(fmt(total() - paidTotal()))
            .append(" €\nNombre de paiements : ")
            .append(installments)
            .append("\n");

        return s.toString();
    }

    void confirmFinish() {
        new AlertDialog.Builder(this)
            .setTitle("Terminer les courses ?")
            .setMessage(
                "Enregistrer le ticket puis vider les achats ?"
            )
            .setPositiveButton("Oui", (d, w) -> {
                history.add(0, ticket());
                items.clear();

                Arrays.fill(card, 0);
                Arrays.fill(cash, 0);

                save();
                tab = "historique";
                show();
            })
            .setNegativeButton("Non", null)
            .show();
    }

    void historique() {
        body.addView(text(
            "Historique des courses", 21
        ));

        if (history.isEmpty()) {
            body.addView(text(
                "Aucun achat enregistré.", 16
            ));
        }

        for (int i = 0; i < history.size(); i++) {
            final int ix = i;
            String h = history.get(i);

            body.addView(text(h, 14));

            Button print = button(
                "🖨️ Imprimer ce ticket"
            );
            body.addView(print);

            print.setOnClickListener(v ->
                printText(
                    h,
                    "Courses historique " + (ix + 1)
                )
            );

            Button del = button(
                "✕ Supprimer ce ticket"
            );
            body.addView(del);

            del.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                    .setTitle(
                        "Supprimer ce ticket ?"
                    )
                    .setPositiveButton(
                        "Oui",
                        (d, w) -> {
                            history.remove(ix);
                            save();
                            show();
                        }
                    )
                    .setNegativeButton(
                        "Non", null
                    )
                    .show()
            );
        }
    }

    void printText(
        String content, String title
    ) {
        PrintManager manager =
            (PrintManager) getSystemService(
                PRINT_SERVICE
            );

        if (manager == null) {
            toast(
                "Service d'impression indisponible"
            );
            return;
        }

        manager.print(
            title,
            new TicketAdapter(content),
            new PrintAttributes.Builder()
                .setMediaSize(
                    PrintAttributes.MediaSize.ISO_A4
                )
                .build()
        );
    }

    class TicketAdapter
        extends PrintDocumentAdapter {

        final String content;
        PdfDocument doc;

        TicketAdapter(String s) {
            content = s;
        }

        @Override
        public void onLayout(
            PrintAttributes oldA,
            PrintAttributes newA,
            android.os.CancellationSignal signal,
            LayoutResultCallback cb,
            Bundle extras
        ) {
            if (signal.isCanceled()) {
                cb.onLayoutCancelled();
                return;
            }

            cb.onLayoutFinished(
                new PrintDocumentInfo.Builder(
                    "courses.pdf"
                )
                    .setContentType(
                        PrintDocumentInfo.CONTENT_TYPE_DOCUMENT
                    )
                    .build(),
                true
            );
        }

        @Override
        public void onWrite(
            PageRange[] pages,
            ParcelFileDescriptor dest,
            android.os.CancellationSignal signal,
            WriteResultCallback cb
        ) {
            doc = new PdfDocument();

            try {
                Paint p = new Paint(
                    Paint.ANTI_ALIAS_FLAG
                );

                int number = 1;
                int y = 55;

                PdfDocument.Page page =
                    doc.startPage(
                        new PdfDocument.PageInfo.Builder(
                            595, 842, number
                        ).create()
                    );

                Canvas canvas = page.getCanvas();

                for (
                    String line :
                    content.split("\n", -1)
                ) {
                    boolean heading =
                        line.startsWith("MAGASIN :")
                        || line.startsWith("SOUS-TOTAL")
                        || line.startsWith("TOTAL ")
                        || line.startsWith(
                            "LISTE COMPLÈTE"
                        )
                        || line.equals("PAIEMENTS")
                        || line.equals(
                            "PRODUITS NON ACHETÉS"
                        );

                    p.setTypeface(
                        Typeface.create(
                            Typeface.DEFAULT,
                            heading
                                ? Typeface.BOLD
                                : Typeface.NORMAL
                        )
                    );

                    p.setTextSize(
                        heading ? 13 : 11
                    );

                    p.setColor(
                        heading
                            ? 0xff146c43
                            : 0xff26352d
                    );

                    ArrayList<String> wrapped =
                        wrap(line, p, 505);

                    for (String part : wrapped) {
                        if (signal.isCanceled()) {
                            doc.close();
                            cb.onWriteCancelled();
                            return;
                        }

                        if (y > 795) {
                            doc.finishPage(page);

                            page = doc.startPage(
                                new PdfDocument.PageInfo.Builder(
                                    595,
                                    842,
                                    ++number
                                ).create()
                            );

                            canvas = page.getCanvas();
                            y = 55;
                        }

                        if (heading) {
                            Paint shade = new Paint();
                            shade.setColor(0xffe9f5ec);

                            canvas.drawRect(
                                36,
                                y - 15,
                                558,
                                y + 6,
                                shade
                            );
                        }

                        canvas.drawText(
                            part, 44, y, p
                        );

                        y += heading ? 23 : 17;
                    }
                }

                doc.finishPage(page);

                try (
                    FileOutputStream out =
                        new FileOutputStream(
                            dest.getFileDescriptor()
                        )
                ) {
                    doc.writeTo(out);
                }

                doc.close();

                cb.onWriteFinished(
                    new PageRange[]{
                        PageRange.ALL_PAGES
                    }
                );

            } catch (Exception e) {
                if (doc != null) doc.close();
                cb.onWriteFailed(e.getMessage());
            }
        }

        ArrayList<String> wrap(
            String line, Paint p, int max
        ) {
            ArrayList<String> result =
                new ArrayList<>();

            if (line.isEmpty()) {
                result.add("");
                return result;
            }

            String rest = line;

            while (!rest.isEmpty()) {
                int count = p.breakText(
                    rest, true, max, null
                );

                if (count <= 0) count = 1;

                if (count < rest.length()) {
                    int space =
                        rest.lastIndexOf(' ', count);

                    if (space > 0) {
                        count = space;
                    }
                }

                result.add(
                    rest.substring(0, count)
                );

                rest = rest.substring(
                    count
                ).trim();
            }

            return result;
        }
    }

    void save() {
        try {
            JSONArray a = new JSONArray();

            for (Item it : items) {
                JSONObject j = new JSONObject();

                j.put("name", it.name);
                j.put("unit", it.unit);
                j.put("emoji", it.emoji);
                j.put("qty", it.qty);
                j.put("price", it.price);
                j.put("bought", it.bought);
                j.put("store", it.store);

                a.put(j);
            }

            JSONArray h = new JSONArray();

            for (String s : history) {
                h.put(s);
            }

            JSONArray payments =
                new JSONArray();

            for (int i = 0; i < 3; i++) {
                JSONObject j = new JSONObject();

                j.put("card", card[i]);
                j.put("cash", cash[i]);

                payments.put(j);
            }

            JSONArray shops = new JSONArray();

            for (String s : customStores) {
                shops.put(s);
            }

            getSharedPreferences(
                "courses", MODE_PRIVATE
            ).edit()
                .putString(
                    "items", a.toString()
                )
                .putString(
                    "history", h.toString()
                )
                .putString(
                    "date", date
                )
                .putInt(
                    "installments", installments
                )
                .putString(
                    "payerAmounts",
                    payments.toString()
                )
                .putString(
                    "customStores",
                    shops.toString()
                )
                .apply();

        } catch (Exception e) {
            android.util.Log.e(
                "ListeCourses",
                "Sauvegarde impossible",
                e
            );
        }
    }

    void load() {
        try {
            android.content.SharedPreferences p =
                getSharedPreferences(
                    "courses", MODE_PRIVATE
                );

            date = p.getString(
                "date", date
            );

            installments = p.getInt(
                "installments", 1
            );

            JSONArray a = new JSONArray(
                p.getString("items", "[]")
            );

            for (int i = 0; i < a.length(); i++) {
                JSONObject j = a.getJSONObject(i);

                Item it = new Item(
                    j.getString("name"),
                    j.getString("unit"),
                    j.optString("emoji", "🛍️"),
                    j.getDouble("qty"),
                    j.getDouble("price"),
                    j.getBoolean("bought")
                );

                it.store = j.optString(
                    "store", ""
                );

                items.add(it);
            }

            JSONArray h = new JSONArray(
                p.getString("history", "[]")
            );

            for (int i = 0; i < h.length(); i++) {
                history.add(h.getString(i));
            }

            JSONArray payments = new JSONArray(
                p.getString(
                    "payerAmounts", "[]"
                )
            );

            for (
                int i = 0;
                i < Math.min(3, payments.length());
                i++
            ) {
                JSONObject j =
                    payments.getJSONObject(i);

                card[i] = j.optDouble(
                    "card", 0
                );

                cash[i] = j.optDouble(
                    "cash", 0
                );
            }

            JSONArray shops = new JSONArray(
                p.getString(
                    "customStores", "[]"
                )
            );

            for (
                int i = 0;
                i < shops.length();
                i++
            ) {
                customStores.add(
                    shops.getString(i)
                );
            }

        } catch (Exception e) {
            android.util.Log.e(
                "ListeCourses",
                "Chargement partiel",
                e
            );
        }
    }

    @Override
    protected void onActivityResult(
        int requestCode,
        int resultCode,
        Intent data
    ) {
        IntentResult result =
            IntentIntegrator.parseActivityResult(
                requestCode,
                resultCode,
                data
            );

        if (result != null) {
            if (result.getContents() != null) {
                String barcode =
                    result.getContents();

                EditText name = field(
                    "Nom du produit",
                    barcode,
                    false
                );

                EditText price = field(
                    "Prix du magasin (€)",
                    "",
                    true
                );

                LinearLayout layout = vertical();
                layout.setPadding(
                    dp(20), 0, dp(20), 0
                );

                layout.addView(text(
                    "Code scanné : "
                    + barcode
                    + "\nLe code-barres seul ne fournit pas le prix.",
                    14
                ));

                layout.addView(name);
                layout.addView(price);

                new AlertDialog.Builder(this)
                    .setTitle("Produit scanné")
                    .setView(layout)
                    .setPositiveButton(
                        "Ajouter",
                        (d, w) -> {
                            String n = name.getText()
                                .toString().trim();

                            double p = parse(
                                price.getText()
                                    .toString()
                            );

                            if (
                                !n.isEmpty()
                                && p >= 0
                            ) {
                                items.add(
                                    new Item(
                                        n,
                                        "pièce",
                                        "🛍️",
                                        1,
                                        p,
                                        true
                                    )
                                );

                                save();
                                show();
                            }
                        }
                    )
                    .setNegativeButton(
                        "Annuler", null
                    )
                    .show();
            }
        } else {
            super.onActivityResult(
                requestCode,
                resultCode,
                data
            );
        }
    }
}

// app/src/main/java/com/artkadvision/listecourses/MainActivity.java
