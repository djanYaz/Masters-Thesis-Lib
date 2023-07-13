import {Genre} from "./genre";
import {Stock} from "./Stock";

export class Book {
  id: number;
  title: string;
  author: string;
  yearPublished: number;
  genre: Genre;
  stock: Stock;
}
