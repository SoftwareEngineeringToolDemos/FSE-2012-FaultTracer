package pkg1;


	public class ClassUnderTest {

		public static int mid(int x, int y, int z) {
			int mid;
			mid = z;
			if (y < z) {
				if (x < y)
					mid = y;
				else if (x < z)
					mid = x;
			} else {
				if (x > y)
					mid = y;
				else if (x > z)
					mid = x;
			}
			return mid;
		}
	}

