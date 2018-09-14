import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'moneyFormat' })
export class MoneyFormatPipe implements PipeTransform {
    transform(value: number, exponent?: string): string {
        switch (value) {
            case 10000:
                return '10,000 đ';
            case 20000:
                return '20,000 đ';
            case 30000:
                return '30,000 đ';
            case 40000:
                return '40,000 đ';
            case 50000:
                return '50,000 đ';
            case 100000:
                return '100,000 đ';
            case 200000:
                return '200,000 đ';
            case 500000:
                return '500,000 đ';
            case 1000000:
                return '1,000,000 đ';
        }
    }
}
