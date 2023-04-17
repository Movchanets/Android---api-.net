using System.Drawing;
using System.Drawing.Imaging;

namespace Infrastructure.Helper;

public static class ImageWorker
{
    public static Bitmap FromBase64StringToImage(this string base64String)
    {
        var byteBuffer = Convert.FromBase64String(base64String);
        try
        {
            using (var memoryStream = new MemoryStream(byteBuffer))
            {
                memoryStream.Position = 0;
                Image imgReturn;
                imgReturn = Image.FromStream(memoryStream);
                memoryStream.Close();
                byteBuffer = null;
                return new Bitmap(imgReturn);
            }
        }
        catch
        {
            return null;
        }
    }

    public static string SaveImage(string imageBase64)
    {
        var fileName = Path.GetRandomFileName() + ".jpg";
        try
        {
            var base64 = imageBase64;
            if (base64.Contains(','))
                base64 = base64.Split(',')[1];

            var img = base64.FromBase64StringToImage();
            var dirSave = Path.Combine(Directory.GetCurrentDirectory(), "images", fileName);
            var saveImage = CompressImage(img, 1200, 1200, false);
            saveImage.Save(dirSave, ImageFormat.Jpeg);
        }
        catch
        {
            throw new Exception("Файл не вдалося зберегти!");
        }

        return fileName;
    }

    public static Bitmap CompressImage(Bitmap originalPic, int maxWidth, int maxHeight, bool watermark = true,
        bool transperent = false)
    {
        try
        {
            var width = originalPic.Width;
            var height = originalPic.Height;
            var widthDiff = width - maxWidth;
            var heightDiff = height - maxHeight;
            var doWidthResize = (maxWidth > 0 && width > maxWidth && widthDiff > heightDiff);
            var doHeightResize = (maxHeight > 0 && height > maxHeight && heightDiff > widthDiff);

            if (doWidthResize || doHeightResize || (width.Equals(height) && widthDiff.Equals(heightDiff)))
            {
                int iStart;
                Decimal divider;
                if (doWidthResize)
                {
                    iStart = width;
                    divider = Math.Abs((Decimal)iStart / maxWidth);
                    width = maxWidth;
                    height = (int)Math.Round((height / divider));
                }
                else
                {
                    iStart = height;
                    divider = Math.Abs((Decimal)iStart / maxHeight);
                    height = maxHeight;
                    width = (int)Math.Round(width / divider);
                }
            }

            using (var outBmp = new Bitmap(width, height, PixelFormat.Format24bppRgb))
            {
                using (var oGraphics = Graphics.FromImage(outBmp))
                {
                    //oGraphics.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.AntiAlias;
                    //oGraphics.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.HighQualityBicubic;
                    oGraphics.Clear(Color.White);
                    oGraphics.DrawImage(originalPic, 0, 0, width, height);
                    //Watermark
                    if (watermark)
                    {
                        using (var watermarkImage = Image.FromFile(Path.Combine(Directory.GetCurrentDirectory(),
                                   "images", "lohika_watermark.png")))
                        using (var watermarkBrush = new TextureBrush(watermarkImage))
                        {
                            //Not responsive
                            //int x = (outBmp.Width - watermarkImage.Width - 15);
                            //int y = (outBmp.Height - watermarkImage.Height -15);
                            var imageHeightBrand = Convert.ToDouble(watermarkImage.Height);
                            var imageWidthBrand = Convert.ToDouble(watermarkImage.Width);
                            var ratioBrand = imageWidthBrand / imageHeightBrand;

                            var imageHeightBild = Convert.ToDouble(outBmp.Height); //height of the image to watermark
                            var imageWidthBild = Convert.ToDouble(outBmp.Width);
                            var imageWidthTmpBranding =
                                imageWidthBild * 0.2; //the watermark width, but only 20% size of the image to watermark
                            var imageHeightTmpBranding =
                                imageWidthTmpBranding / ratioBrand; //height of watermark, preserve aspect ratio
                            var imageWidthBranding =
                                Convert.ToInt32(imageWidthTmpBranding); //convert in into int32 (see method below)
                            var imageHeightBranding = Convert.ToInt32(imageHeightTmpBranding);

                            var watermarkX = (int)(imageWidthBild - imageWidthBranding); // Bottom Right
                            var watermarkY = (int)(imageHeightBild - imageHeightBranding);
                            oGraphics.DrawImage(watermarkImage,
                                new Rectangle(watermarkX, watermarkY, imageWidthBranding, imageHeightBranding),
                                new Rectangle(0, 0, (int)imageWidthBrand, (int)imageHeightBrand),
                                GraphicsUnit.Pixel);
                            //watermarkBrush.TranslateTransform(watermarkX, watermarkY);
                            //oGraphics.FillRectangle(watermarkBrush, new Rectangle(new Point(watermarkX, watermarkY), new Size(watermarkImage.Width, watermarkImage.Height)));
                        }
                    }

                    if (transperent)
                    {
                        outBmp.MakeTransparent();
                    }

                    return new Bitmap(outBmp);
                }
            }
        }
        catch
        {
            return null;
        }
    }

    public static void RemoveImage(string fileName)
    {
        try
        {
            var file = Path.Combine(Directory.GetCurrentDirectory(), "images", fileName);
            if (System.IO.File.Exists(file))
            {
                System.IO.File.Delete(file);
            }
        }
        catch
        {
        }
    }
}