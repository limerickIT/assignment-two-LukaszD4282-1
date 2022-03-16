using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Net;
using System.IO;

namespace RestAPICall
{
    class Program
    {

        static int beerID = 1;
        static void Main(string[] args)
        {
            getBeerData();
        }

        public static void getBeerData()
        {
            string strurl = String.Format("http://localhost:8888/beers/getBeer/" + beerID + "/");
            //Console.WriteLine("CALL: " + strurl);
            WebRequest requestObject = WebRequest.Create(strurl);
            requestObject.Method = "GET";
            HttpWebResponse responseObject = null;
            responseObject = (HttpWebResponse)requestObject.GetResponse();

            string strResult = null;
            using (Stream stream = responseObject.GetResponseStream())
            {
                StreamReader sr = new StreamReader(stream);
                strResult = sr.ReadToEnd();
                sr.Close();

                Console.WriteLine("\nJSON result:\n");
                Console.WriteLine(strResult + "\n");
            }

        }
    }
}
