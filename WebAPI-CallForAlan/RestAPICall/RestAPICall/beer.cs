using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RestAPICall
{
    public class Beer
    {
        public long id { get; set; }
        public long brewery_id { get; set; }
        public string name { get; set; }
        public int cat_id { get; set; }
        public int style_id { get; set; }
        public double abv { get; set; }
        public double ibu { get; set; }
        public double srm { get; set; }
        public string description { get; set; }
        public int add_user { get; set; }
        public DateTime last_mod { get; set; }
        public String image { get; set; }
        public double buy_price { get; set; }
        public double sell_price { get; set; }

        public Beer(long brewery_id, string name, int cat_id, int style_id, double abv, double ibu, double srm, double buy_price, double sell_price, int add_user, string description)
        {
            this.brewery_id = brewery_id;
            this.name = name;
            this.cat_id = cat_id;
            this.style_id = style_id;
            this.abv = abv;
            this.ibu = ibu;
            this.srm = srm;
            this.buy_price = buy_price;
            this.sell_price = sell_price;
            this.add_user = add_user;
            this.description = description;
        }
    }
}
