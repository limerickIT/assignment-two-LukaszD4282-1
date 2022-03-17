using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.Net;
using System.IO;
using System.Text.Json;

namespace RestAPICall
{
    class Program
    {

        static int beerID = 1;
        static void Main(string[] args)
        {
            insertBeer();
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

        public static void insertBeer()
        {
            Random rnd = new Random();
            Beer nb = new Beer(812,"New beer", 11, 116, 5, 4.6, 2.5, 5, 7, 0, "New description");
            string postData = JsonSerializer.Serialize<Beer>(nb);

            string strurl = "http://localhost:8888/beers/createBeer/";
            Console.WriteLine(postData);
            WebRequest requestObject = WebRequest.Create(strurl);
            requestObject.Method = "POST";
            requestObject.ContentType = "application/json";

           string strResult = null;
            using (var streamWriter = new StreamWriter(requestObject.GetRequestStream()))
            {
                streamWriter.Write(postData);
                streamWriter.Flush();
                streamWriter.Close();

                var httpResponse = (HttpWebResponse)requestObject.GetResponse();

                using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
                {
                    var result = streamReader.ReadToEnd();
                }

                    Console.WriteLine("\nJSON result:\n");
                Console.WriteLine(strResult + "\n");
            }
        }
    }
}
