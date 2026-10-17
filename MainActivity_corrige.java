package com.artkadvision.listecourses;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import android.text.InputType;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    final String[] categories={"Fruits","Légumes","Viandes et volailles","Poissons","Épicerie et pâtes","Conserves","Produits frais","Surgelés et glaces","Cuisine et entretien","Lessive","Toilettes et douche","Hygiène féminine","Vêtements","Autres"};
    final String[][] products={
        {"🍎|Pommes|kg","🍌|Bananes|kg","🍊|Oranges|kg","🍐|Poires|kg","🍇|Raisins|kg"},
        {"🥕|Carottes|kg","🥔|Pommes de terre|kg","🍅|Tomates|kg","🥬|Salade|pièce","🧅|Oignons|kg"},
        {"🍗|Poulet|kg","🥩|Bœuf|kg","🍖|Agneau|kg","🥚|Œufs|pièce"},
        {"🐟|Poisson frais|kg","🦐|Crevettes|kg","🐠|Filets de poisson|kg"},
        {"🍝|Pâtes|paquet","🍚|Riz|kg","🧂|Sel|paquet","🫒|Huile|L","🍞|Pain|pièce","🥣|Farine|kg"},
        {"🥫|Tomates en conserve|boîte","🥫|Thon|boîte","🥫|Petits pois|boîte","🥫|Haricots|boîte"},
        {"🥛|Lait|L","🧀|Fromage|pièce","🧈|Beurre|pièce","🥛|Yaourts|pièce"},
        {"🍦|Glaces|boîte","🧊|Légumes surgelés|kg","🍕|Pizza surgelée|pièce"},
        {"🧽|Éponges|pièce","🧴|Liquide vaisselle|L","🧻|Essuie-tout|rouleau","🧹|Nettoyant cuisine|L"},
        {"🧺|Lessive|L","🧴|Adoucissant|L","🧼|Détachant|pièce"},
        {"🧴|Shampooing|L","🧼|Savon|pièce","🚿|Gel douche|L","🪥|Dentifrice|pièce","🧻|Papier toilette|rouleau"},
        {"🌸|Serviettes hygiéniques|paquet","🌸|Tampons|boîte","🧴|Soin féminin|pièce"},
        {"👕|T-shirt|pièce","👖|Pantalon|pièce","🧦|Chaussettes|paire","👟|Chaussures|paire"},
        {"🛍️|Autre produit|pièce"}
    };
    LinearLayout root, body;
    TextView summary;
    Spinner categoryPicker;
    final ArrayList<Item> items=new ArrayList<>();
    final ArrayList<String> history=new ArrayList<>();
    String activeTab="catalogue";
    String payment="Carte bancaire";
    int installments=1;
    String date;
    static class Item {String name,unit,emoji;double qty,price;boolean bought;Item(String n,String u,String e,double q,double p,boolean b){name=n;unit=u;emoji=e;qty=q;price=p;bought=b;}}
    @Override public void onCreate(Bundle b){super.onCreate(b);date=new SimpleDateFormat("dd/MM/yyyy",Locale.FRANCE).format(new Date());load();show();}
    int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    TextView text(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextColor(Color.rgb(35,45,38));t.setTextSize(size);t.setPadding(dp(7),dp(9),dp(7),dp(9));return t;}
    Button button(String s){Button b=new Button(this);b.setText(s);b.setAllCaps(false);return b;}
    EditText field(String hint,String value,boolean numeric){EditText e=new EditText(this);e.setSingleLine(true);e.setHint(hint);e.setText(value);if(numeric)e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);return e;}
    LinearLayout vertical(){LinearLayout l=new LinearLayout(this);l.setOrientation(1);return l;}
    void show(){
        ScrollView scroll=new ScrollView(this);root=vertical();root.setPadding(dp(10),dp(8),dp(10),dp(16));root.setBackgroundColor(Color.rgb(249,250,246));scroll.addView(root);setContentView(scroll);
        TextView title=text("🛒  Liste Courses",26);title.setGravity(Gravity.CENTER);title.setTypeface(null,1);root.addView(title);
        LinearLayout tabs=new LinearLayout(this);root.addView(tabs);
        for(String[] tab:new String[][]{{"catalogue","📋 Ma liste"},{"achats","🧮 Achats"},{"historique","🗂 Historique"}}){Button b=button(tab[1]);tabs.addView(b,new LinearLayout.LayoutParams(0,dp(55),1));b.setEnabled(!activeTab.equals(tab[0]));b.setOnClickListener(v->{activeTab=tab[0];show();});}
        body=vertical();root.addView(body);
        if(activeTab.equals("catalogue"))catalogue();else if(activeTab.equals("achats"))achats();else historique();
    }
    void catalogue(){
        body.addView(text("Choisis une catégorie, puis tes produits.",16));
        categoryPicker=new Spinner(this);ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,categories);categoryPicker.setAdapter(adapter);body.addView(categoryPicker);
        LinearLayout list=vertical();body.addView(list);
        categoryPicker.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){list.removeAllViews();for(String raw:products[pos]){String[] parts=raw.split("\\|");productRow(list,parts[1],parts[2],parts[0]);}}});
        body.addView(text("Ajouter un produit personnel",18));
        EditText name=field("Nom du produit","",false);body.addView(name);
        Spinner units=new Spinner(this);String[] unitList={"pièce","kg","g","L","mL","paquet","boîte","rouleau","paire"};units.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,unitList));body.addView(units);
        Button add=button("+ Ajouter à ma liste");body.addView(add);add.setOnClickListener(v->{String n=name.getText().toString().trim();if(n.isEmpty()){toast("Entre un nom");return;}addItem(n,units.getSelectedItem().toString(),"🛍️",1);name.setText("");});
        body.addView(text("Ma liste préparée : "+items.size()+" ligne(s)",18));
        for(int i=0;i<items.size();i++){final int index=i;Item item=items.get(i);LinearLayout line=new LinearLayout(this);line.setGravity(Gravity.CENTER_VERTICAL);body.addView(line);TextView label=text(item.emoji+" "+item.name+" — "+fmt(item.qty)+" "+item.unit,15);line.addView(label,new LinearLayout.LayoutParams(0,-2,1));Button del=button("✕");line.addView(del);del.setOnClickListener(v->{items.remove(index);save();show();});}
    }
    void productRow(LinearLayout parent,String name,String unit,String emoji){
        LinearLayout line=new LinearLayout(this);line.setGravity(Gravity.CENTER_VERTICAL);parent.addView(line);
        CheckBox check=new CheckBox(this);check.setText(emoji+"  "+name);check.setTextSize(16);line.addView(check,new LinearLayout.LayoutParams(0,dp(58),1));
        EditText qty=field("Qté","1",true);line.addView(qty,new LinearLayout.LayoutParams(dp(58),dp(54)));TextView unitText=text(unit,12);line.addView(unitText);
        check.setOnCheckedChangeListener((b,checked)->{if(checked){double q=parse(qty.getText().toString());if(q<=0){check.setChecked(false);toast("Quantité invalide");return;}addItem(name,unit,emoji,q);check.setChecked(false);}});
    }
    void addItem(String name,String unit,String emoji,double qty){items.add(new Item(name,unit,emoji,qty,0,false));save();toast(name+" ajouté");}
    void achats(){
        body.addView(text("Date : "+date,17));Button changeDate=button("📅 Changer la date");body.addView(changeDate);changeDate.setOnClickListener(v->{Calendar c=Calendar.getInstance();new DatePickerDialog(this,(view,y,m,d)->{date=String.format(Locale.FRANCE,"%02d/%02d/%04d",d,m+1,y);save();show();},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();});
        summary=text("",19);body.addView(summary);
        if(items.isEmpty())body.addView(text("Ta liste est vide. Ajoute des produits dans Ma liste.",16));
        for(int i=0;i<items.size();i++)purchaseRow(i);
        Button scan=button("📷 Scanner un code-barres");body.addView(scan);scan.setOnClickListener(v->{IntentIntegrator it=new IntentIntegrator(this);it.setPrompt("Scanner le code-barres");it.setBeepEnabled(false);it.initiateScan();});
        body.addView(text("Saisie libre d'un achat",18));EditText name=field("Produit","",false);body.addView(name);EditText q=field("Quantité","1",true);body.addView(q);EditText price=field("Prix unitaire (€)","",true);body.addView(price);
        Button add=button("+ Enregistrer cet achat");body.addView(add);add.setOnClickListener(v->{String n=name.getText().toString().trim();if(n.isEmpty())return;double quantity=parse(q.getText().toString());if(quantity<=0){toast("Quantité invalide");return;}items.add(new Item(n,"pièce","🛍️",quantity,Math.max(0,parse(price.getText().toString())),true));save();show();});
        body.addView(text("Moyen de paiement",19));Spinner methods=new Spinner(this);String[] choices={"Carte bancaire","Espèces","Chèque","Virement","Autre"};methods.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,choices));for(int i=0;i<choices.length;i++)if(choices[i].equals(payment))methods.setSelection(i);body.addView(methods);methods.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){payment=choices[pos];save();}});
        body.addView(text("Nombre de paiements (1 = en une fois)",16));Spinner split=new Spinner(this);String[] counts={"1","2","3","4","5","6","10","12"};split.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,counts));for(int i=0;i<counts.length;i++)if(Integer.parseInt(counts[i])==installments)split.setSelection(i);body.addView(split);split.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){installments=Integer.parseInt(counts[pos]);save();refreshSummary();}});
        Button finish=button("✅ Enregistrer les courses dans l'historique");body.addView(finish);finish.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Terminer les courses ?").setMessage("Enregistrer le ticket puis vider les achats ?").setPositiveButton("Oui",(d,w)->enregistrerCourses()).setNegativeButton("Non",null).show());
        refreshSummary();
    }
    void purchaseRow(int index){Item it=items.get(index);LinearLayout row=vertical();row.setPadding(dp(4),dp(6),dp(4),dp(6));body.addView(row);CheckBox bought=new CheckBox(this);bought.setText(it.emoji+" "+it.name);bought.setTextSize(17);bought.setChecked(it.bought);row.addView(bought);bought.setOnCheckedChangeListener((b,c)->{it.bought=c;save();refreshSummary();});LinearLayout fields=new LinearLayout(this);row.addView(fields);
        EditText quantity=field("Qté",fmt(it.qty),true);fields.addView(quantity,new LinearLayout.LayoutParams(0,dp(54),1));TextView unit=text(it.unit,12);fields.addView(unit);EditText price=field("Prix €",it.price==0?"":fmt(it.price),true);fields.addView(price,new LinearLayout.LayoutParams(0,dp(54),1));Button del=button("✕");fields.addView(del);del.setOnClickListener(v->{items.remove(index);save();show();});
        quantity.setOnFocusChangeListener((v,focus)->{if(!focus){double n=parse(quantity.getText().toString());if(n>0){it.qty=n;save();refreshSummary();}}});price.setOnFocusChangeListener((v,focus)->{if(!focus){it.price=Math.max(0,parse(price.getText().toString()));save();refreshSummary();}});
    }
    void refreshSummary(){if(summary==null||!activeTab.equals("achats"))return;double total=0;int lines=0;double units=0;for(Item i:items)if(i.bought){total+=i.qty*i.price;lines++;units+=i.qty;}summary.setText("Produits achetés : "+lines+" lignes | Quantités : "+fmt(units)+" (unités variées)\nTOTAL : "+fmt(total)+" €\n"+installments+" paiement(s) : environ "+fmt(total/installments)+" € chacun");}
    void enregistrerCourses(){double total=0;int count=0;StringBuilder detail=new StringBuilder();for(Item i:items)if(i.bought){total+=i.qty*i.price;count++;detail.append("\n").append(i.name).append(" : ").append(fmt(i.qty)).append(" ").append(i.unit).append(" × ").append(fmt(i.price)).append(" €");}if(count==0){toast("Aucun achat coché");return;}history.add(0,date+" | "+count+" produits | "+fmt(total)+" €\nPaiement : "+payment+" | "+installments+" fois"+detail);items.clear();save();activeTab="historique";show();}
    void historique(){body.addView(text("Historique des courses",21));if(history.isEmpty())body.addView(text("Aucun achat enregistré.",16));for(String h:history){TextView t=text(h,15);body.addView(t);View line=new View(this);line.setBackgroundColor(0xffcccccc);body.addView(line,new LinearLayout.LayoutParams(-1,dp(1)));}}
    double parse(String s){try{return Double.parseDouble(s.trim().replace(',','.'));}catch(Exception e){return 0;}}
    String fmt(double d){return String.format(Locale.FRANCE,"%.2f",d);}
    void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
    void save(){try{JSONArray a=new JSONArray();for(Item i:items){JSONObject j=new JSONObject();j.put("name",i.name);j.put("unit",i.unit);j.put("emoji",i.emoji);j.put("qty",i.qty);j.put("price",i.price);j.put("bought",i.bought);a.put(j);}JSONArray h=new JSONArray();for(String s:history)h.put(s);getSharedPreferences("courses",MODE_PRIVATE).edit().putString("items",a.toString()).putString("history",h.toString()).putString("date",date).putString("payment",payment).putInt("installments",installments).apply();}catch(Exception ignored){}}
    void load(){try{android.content.SharedPreferences p=getSharedPreferences("courses",MODE_PRIVATE);date=p.getString("date",date);payment=p.getString("payment",payment);installments=p.getInt("installments",1);JSONArray a=new JSONArray(p.getString("items","[]"));for(int x=0;x<a.length();x++){JSONObject j=a.getJSONObject(x);items.add(new Item(j.getString("name"),j.getString("unit"),j.optString("emoji","🛍️"),j.getDouble("qty"),j.getDouble("price"),j.getBoolean("bought")));}JSONArray h=new JSONArray(p.getString("history","[]"));for(int x=0;x<h.length();x++)history.add(h.getString(x));}catch(Exception ignored){}}
    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data){IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);if(result!=null){if(result.getContents()!=null){final String barcode=result.getContents();EditText name=field("Nom du produit",barcode,false);EditText price=field("Prix du magasin (€)","",true);LinearLayout layout=vertical();layout.setPadding(dp(20),0,dp(20),0);layout.addView(text("Code scanné : "+barcode+"\nLe code-barres seul ne fournit pas le prix.",14));layout.addView(name);layout.addView(price);new AlertDialog.Builder(this).setTitle("Produit scanné").setView(layout).setPositiveButton("Ajouter",(d,w)->{String n=name.getText().toString().trim();if(!n.isEmpty()){items.add(new Item(n,"pièce","🛍️",1,Math.max(0,parse(price.getText().toString())),true));save();show();}}).setNegativeButton("Annuler",null).show();}}else super.onActivityResult(requestCode,resultCode,data);}
}
