object Roman extends App {
  def toRoman(n: Int): String = n match{
      case 0 => ""
      case n if n-400<100 && n-400>=0  => toRoman(100)+toRoman(n+100)
      case n if n-900<100 && n-900>=0 => toRoman(100)+toRoman(n+100)
      case n if n-40<10 && n-40>=0 => toRoman(10)+toRoman(n+10)
      case n if n-90<10 && n-90>=0 => toRoman(10)+toRoman(n+10)
      case n if n==4||n==9 => toRoman(1)+toRoman(n+1)
      case n if n-1000 >= 0 => "M"+toRoman(n-1000)
      case n if n-500 >= 0 => "D"+toRoman(n-500)
      case n if n-100 >= 0 => "C"+toRoman(n-100)
      case n if n-50 >= 0 => "L"+toRoman(n-50)
      case n if n-10 >= 0 => "X"+toRoman(n-10)
      case n if n-5 >= 0 => "V"+toRoman(n-5)
      case n if n-1 >= 0 => "I"+toRoman(n-1)
  }
}
